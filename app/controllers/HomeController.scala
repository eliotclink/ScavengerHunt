package controllers

import domain.GameDefinition
import formats.JsonFormats.given
import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import services.GameService
import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(
  val controllerComponents: ControllerComponents,
  gameService: GameService
)(implicit ec: ExecutionContext) extends BaseController {

  def index() = Action {
    Ok(Json.obj("message" -> "Hello from ScavengerHunt!"))
  }

  def createGameDefinition() = Action.async(parse.json[GameDefinition]) { (request: Request[GameDefinition]) =>
    gameService.createGameDefinition(request.body).map { _ =>
      Created(Json.obj("id" -> request.body.id.toString))
    }
  }
}
