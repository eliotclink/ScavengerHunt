package controllers

import domain.{GameDefinition, User}
import formats.JsonFormats.given
import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import services.{GameService, UserService}
import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(
  val controllerComponents: ControllerComponents,
  gameService: GameService,
  userService: UserService
)(implicit ec: ExecutionContext) extends BaseController {

  def index() = Action {
    Ok(Json.obj("message" -> "Hello from ScavengerHunt!"))
  }

  def createGameDefinition() = Action.async(parse.json[GameDefinition]) { (request: Request[GameDefinition]) =>
    gameService.createGameDefinition(request.body).map { _ =>
      Created(Json.obj("id" -> request.body.id.toString))
    }
  }

  def createUser() = Action.async(parse.json[User]) { (request: Request[User]) =>
    userService.createUser(request.body).map { _ =>
      Created(Json.obj("id" -> request.body.id.toString))
    }
  }
}
