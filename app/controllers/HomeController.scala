package controllers

import domain.{Game, GameDefinition, User}
import formats.JsonFormats.given
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{Action, BaseController, ControllerComponents, Request}
import services.{GameDefinitionNotFound, GameService, UserService}
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

  def createGame() = Action.async(parse.json[Game]) { (request: Request[Game]) =>
    gameService.createGame(request.body).map { _ =>
      Created(Json.obj("id" -> request.body.id.toString))
    }.recover {
      case GameDefinitionNotFound(id) =>
        UnprocessableEntity(Json.obj("error" -> s"GameDefinition not found: $id"))
    }
  }

  def createUser() = Action.async(parse.json[User]) { (request: Request[User]) =>
    userService.createUser(request.body).map { _ =>
      Created(Json.obj("id" -> request.body.id.toString))
    }
  }
}
