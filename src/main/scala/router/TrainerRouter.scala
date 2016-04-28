package router

import spray.routing._

trait TrainerRouter extends HttpService {
  val trainerqOperations: Route = trainerq
  private def trainerq = {
    get{
      path("trainerq"){
        detach(){
          complete("Trainer Q")
        }
      }
    }
  }
}
