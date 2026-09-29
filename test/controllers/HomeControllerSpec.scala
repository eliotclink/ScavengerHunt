package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "POST /game-definitions" should {
    "return 201 with the generated id" in {
      val body = Json.obj(
        "title"      -> "Test Hunt",
        "definition" -> "A test scavenger hunt",
        "gameType"   -> "TREASURE_HUNT"
      )
      val request = FakeRequest(POST, "/game-definitions")
        .withJsonBody(body)
      val result = route(app, request).get

      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      (contentAsJson(result) \ "id").asOpt[String] mustBe defined
    }
  }

  "GET /" should {
    "return 200 with hello message" in {
      val result = route(app, FakeRequest(GET, "/")).get

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      (contentAsJson(result) \ "message").as[String] mustBe "Hello from ScavengerHunt!"
    }
  }
}
