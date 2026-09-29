package controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action {
    Ok(Json.obj("message" -> "Hello from ScavengerHunt!"))
  }
}
