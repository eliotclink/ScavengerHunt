package controllers

import domain.GameDefinition
import formats.JsonFormats.given
import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import services.GameService

@Singleton
class HomeController @Inject()(
  val controllerComponents: ControllerComponents,
  gameService: GameService
) extends BaseController {

  def index() = Action {
    Ok(Json.obj("message" -> "Hello from ScavengerHunt!"))
  }

  def createGameDefinition() = Action(parse.json[GameDefinition]) { (request: Request[GameDefinition]) =>
    gameService.createGameDefinition(request.body)
    Created(Json.obj("id" -> request.body.id.toString))
  }
}
