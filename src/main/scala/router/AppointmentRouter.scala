package router

import spray.routing.{ HttpService, Route }

trait AppointmentRouter extends HttpService {
  val apptOperations: Route = sayGoodbye
  private def sayGoodbye = {
    get{
      path("saybye"){
        detach(){
          complete("Goodbye Mate")
        }
      }
    }
  }
}
