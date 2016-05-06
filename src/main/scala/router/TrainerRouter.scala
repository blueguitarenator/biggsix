package router

import com.typesafe.scalalogging.LazyLogging
import service.AthleteService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.routing._

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

trait TrainerRouter extends HttpService with LazyLogging {

  self: Authenticator =>

  val athleteService: AthleteService

  val trainerOperations: Route = trainer

  def trainer = path("trainer") {
    import AppointmentJsonProtocol._
    import spray.httpx.SprayJsonSupport._
    get {
      authenticate(basicUserAuthenticator) { authInfo =>
        respondWithMediaType(`application/json`) {
          onComplete(athleteService.getAll) {
            case Success(appts) => complete(appts)
            case Failure(ex) => complete(InternalServerError, s"An error occurred: ${ex.getMessage}")
          }
        }
      }
    }
  }
}
