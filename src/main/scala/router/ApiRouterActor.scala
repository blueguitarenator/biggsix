package router

import akka.actor.{Actor}
import com.typesafe.scalalogging.{LazyLogging}
import service.{AthleteService, UserService}

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class ApiRouterActor(service: UserService, athService: AthleteService) extends Actor with LazyLogging with ExampleRouter with AthleteRouter with TrainerRouter with UserRouter with Authenticator {

  override val userService = service
  override val athleteService = athService

  // supplies the default dispatcher as the implicit execution context
  override implicit lazy val executionContext = context.dispatcher

//  val swaggerService = new SwaggerHttpService {
//    override def apiTypes = Seq(typeOf[UserRouterDoc])
//    override def apiVersion = "0.1"
//    override def baseUrl = "/" // let swagger-ui determine the host and port
//    override def docsPath = "api-docs"
//    override def actorRefFactory = context
//    override def apiInfo = Some(new ApiInfo("Api users", "", "", "", "", ""))
//  }

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(
    exampleRoutes ~
    dashboardOperations ~
      trainerOperations ~
    userOperations
//    swaggerService.routes
//    get {
//      pathPrefix("") { pathEndOrSingleSlash {
//          getFromResource("swagger-ui/index.html")
//        }
//      } ~
//      pathPrefix("webjars") {
//        getFromResourceDirectory("META-INF/resources/webjars")
//      }
//    }
  )

}