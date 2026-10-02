package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.test.{FakeRequest, Injecting}
import play.api.test.Helpers.{GET, POST, CREATED, OK, contentAsJson, contentType, route, status, writeableOf_AnyContentAsJson, writeableOf_AnyContentAsEmpty, defaultAwaitTimeout}

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "POST /game-definitions" should {
    "return 201 with the generated id" in {
      val step = Json.obj(
        "clue"      -> "Find the red door",
        "solution"  -> "red_door",
        "hint"      -> "Look near the entrance",
        "hintIcon"  -> "door_icon",
        "inputType" -> "QR_SCAN",
        "location"  -> Json.obj("longitude" -> -0.1276, "latitude" -> 51.5074)
      )
      val body = Json.obj(
        "title"      -> "Test Hunt",
        "definition" -> "A test scavenger hunt",
        "gameType"   -> "TREASURE_HUNT",
        "steps"      -> Json.arr(step)
      )
      val request = FakeRequest(POST, "/game-definitions")
        .withJsonBody(body)
      val result = route(app, request).get

      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      (contentAsJson(result) \ "id").asOpt[String] mustBe defined
    }
  }

  "POST /users" should {
    "return 201 with the generated id" in {
      val body = Json.obj(
        "name" -> "Alice",
        "icon" -> "avatar_1"
      )
      val request = FakeRequest(POST, "/users")
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
