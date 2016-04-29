package router

import service.AthleteService
import spray.http.MediaTypes._
import spray.http.StatusCodes._
import spray.routing._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

trait AthleteRouter extends HttpService {
  self: Authenticator =>

  val athleteService: AthleteService

  val dashboardOperations: Route = dashboard

  def dashboard = path("dashboard") {
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
