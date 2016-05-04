package router

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.Actor
import akka.event.Logging
import com.github.kikuomax.spray.jwt.{JwtDirectives, JwtSignature}
import com.github.kikuomax.spray.jwt.JwsExtractor.extractJwsFromCookie
import com.github.kikuomax.spray.jwt.JwtClaimBuilder._
import com.github.kikuomax.spray.jwt.JwtClaimVerifier._
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jwt.JWTClaimsSet
import _root_.dao.Tables.PersonRow
import service.UserService

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt
import scala.collection.mutable.ArrayBuffer
import spray.http.HttpCookie
import spray.http.HttpHeaders.{Host, `Access-Control-Allow-Credentials`, `Access-Control-Allow-Headers`, `Access-Control-Allow-Origin`}
import spray.http.{HttpOrigin, SomeOrigins}
import spray.http.MediaTypes.`application/json`
import spray.http.StatusCodes._
import spray.routing.authentication.{BasicAuth, UserPass}
import spray.routing.Directive0
import spray.routing.HttpService
import spray.routing.RejectionHandler

import scala.util.{Failure, Success}

/** An `Actor` that provides [[ExampleRouter]]. */
//class ExampleServiceActor extends Actor with ExampleService {
//  def actorRefFactory = context
//
//  // supplies the default dispatcher as the implicit execution context
//  override implicit lazy val executionContext = context.dispatcher
//
//  // forwards requests to the route
//  def receive = runRoute(route)
//}

/** An example service. */
trait ExampleRouter extends HttpService with JwtDirectives {

  self: Authenticator =>

  // you can use Actor's dispatcher as the execution context
  implicit val executionContext: ExecutionContext

  // imports implicit signing and verification functions in the scope
  val signature = JwtSignature(JWSAlgorithm.HS256, "chiave segreta")
  import signature._

  // an implicit claim set building function
  implicit val claimBuilder: String => Option[JWTClaimsSet] =
    claimSubject[String](identity) &&
      claimIssuer("spray-jwt") &&
      claimExpiration(30.minutes)

  // name of a token cookie
  private val tokenCookieName = "exampleToken"

  private val userService = UserService

  // user authentication function
//  def myUserPassAuthenticator(userPass: Option[UserPass]): Future[Option[String]] = {
////    onComplete(userService.get(0)) {
////      case Success(Some(user)) => Option(user.email)
////      case Success(None) => None
////      case Failure(ex) => None
////    }
//    Future {"asdf"}
//  }

  // HttpOrigin of the client interface
  private val exampleClientOrigin = HttpOrigin("http", Host("localhost", 8081))

  // directive that allows CORS requests
  private val allowCors: Directive0 = respondWithHeaders(
    `Access-Control-Allow-Credentials`(true),
    `Access-Control-Allow-Headers`(Seq("Authorization")),
    `Access-Control-Allow-Origin`(SomeOrigins(Seq(exampleClientOrigin))))

  // records posted messages
  val messages = ArrayBuffer[(String, String)]()

  // returns messages as a JSON object
  def messagesToJSON: String =
    s"""[
  ${ messages.map(m => s"""{ "user": "${ m._1 }", "message": "${ m._2 }" }""").mkString(",") }
]"""

  val exampleRoutes =
    allowCors {
      // catches all rejections to apply `allowCors` to every request
      handleRejections(RejectionHandler.Default) {
        options {
          // accepts preflights
          complete("")
        } ~
          path("login") {
            // autenticates a user and a password

            parameter("cookie" ? false) { usesCookie =>
              authenticate(BasicAuth(
                jwtAuthenticator(userService.myUserPassAuthenticator _), "secure site"))
              { jws =>
                if (usesCookie) {
                  // makes a cookie associated with the token
                  setCookie(HttpCookie(
                    name = tokenCookieName,
                    content = jws.serialize(),
                    httpOnly = true))
                  {
                    complete(s"Access token is in the cookie ($tokenCookieName)")
                  }
                } else {
                  // returns the token as a response
                  complete(jws.serialize())
                }
              }
            }
          } ~
          path("messages") {
            get {
              // privileges GET access to a given user
              // every user has the privilege
              def testGetPrivilege(claim: JWTClaimsSet): Option[String] =
                Option(claim.getSubject())

              // just returns the messages
              parameter("cookie" ? false) { usesCookie =>
                if (usesCookie) {
                  // reads a token from the cookie
                  authorizeToken(
                    extractJwsFromCookie(tokenCookieName),
                    verifyNotExpired && testGetPrivilege)
                  { userName =>
                    respondWithMediaType(`application/json`) {
                      complete(messagesToJSON)
                    }
                  }
                } else {
                  // reads a token from the Authorization header
                  authorizeToken(
                    verifyNotExpired && testGetPrivilege)
                  { userName =>
                    respondWithMediaType(`application/json`) {
                      complete(messagesToJSON)
                    }
                  }
                }
              }
            } ~
              post {
                // privileges POST access to a given user
                // John and Alice have the privilege
                def testPostPrivilege(claim: JWTClaimsSet): Option[String] = {
                  Option(claim.getSubject()) flatMap {
                    case user: String if user == "John" || user == "Alice" || user == "schwep@test.com" =>
                      Some(user)
                    case _ => None
                  }
                }

                // appends a message and returns the updated messages
                entity(as[String]) { message =>
                  parameter("cookie" ? false) { usesCookie =>
                    if (usesCookie) {
                      // reads a token from the cookie
                      authorizeToken(
                        extractJwsFromCookie(tokenCookieName),
                        verifyNotExpired && testPostPrivilege)
                      { userName =>
                        respondWithMediaType(`application/json`) {
                          messages += ((userName, message))
                          complete(messagesToJSON)
                        }
                      }
                    } else {
                      // reads a token from the Authorization header
                      authorizeToken(
                        verifyNotExpired && testPostPrivilege)
                      { userName =>
                        respondWithMediaType(`application/json`) {
                          messages += ((userName, message))
                          complete(messagesToJSON)
                        }
                      }
                    }
                  }
                }
              }
          }
      }
    }
}

