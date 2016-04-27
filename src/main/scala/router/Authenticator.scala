package router

import dao.Tables._
import model.{ AuthInfo }
import service.UserService
import spray.routing.authentication.{ BasicAuth, UserPass }
import spray.routing.directives.AuthMagnet

import scala.concurrent.{ ExecutionContext, Future }

trait Authenticator {

  def userService: UserService

  def basicUserAuthenticator(implicit ec: ExecutionContext): AuthMagnet[AuthInfo] = {
    def validateUser(userPass: Option[UserPass]): Future[Option[AuthInfo]] = {
     userPass.fold[Future[Option[AuthInfo]]]{
       Future.successful(None)
     }{
       userPass =>
         // TODO Better take a look at Scalaz OptionT http://underscore.io/blog/posts/2013/12/20/scalaz-monad-transformers.html#fnref:scalaz-contrib
         userService.getPerson(userPass.user).map(_.map{
           case tup : (PersonRow, PasswordRow) if userService.passwordMatches(tup._2, userPass.pass) => AuthInfo(tup._1)
         })
     }
    }

    def authenticator(userPass: Option[UserPass]): Future[Option[AuthInfo]] = validateUser(userPass)

    BasicAuth(authenticator _, realm = "Private API")
  }
}