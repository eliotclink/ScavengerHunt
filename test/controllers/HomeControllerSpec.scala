package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.{WSAuthScheme, WSClient}
import play.api.test.Helpers.{CREATED, GET, OK, POST, UNPROCESSABLE_ENTITY, await, contentAsJson, contentType, defaultAwaitTimeout, route, status, writeableOf_AnyContentAsEmpty, writeableOf_AnyContentAsJson}
import play.api.test.{FakeRequest, Injecting}

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "POST /game-definitions" should {
    "return 201 with the generated id and store the document" in {
      val request = FakeRequest(POST, "/game-definitions")
        .withJsonBody(gameDefinitionBody)
      val result = route(app, request).get

      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      val id = (contentAsJson(result) \ "id").as[String]

      val stored = fetchDoc(id)
      stored mustBe defined
      gameDefinitionBody.fields.foreach { case (key, value) =>
        (stored.get \ key).as[JsValue] mustBe value
      }
    }
  }

  "POST /games" should {
    "return 201 with the generated id and store the document" in {
      val gameDefinitionId = createGameDefinition()

      val body = Json.obj(
        "gameDefinitionId" -> gameDefinitionId
      )
      val request = FakeRequest(POST, "/games")
        .withJsonBody(body)
      val result = route(app, request).get

      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      val id = (contentAsJson(result) \ "id").as[String]

      val stored = fetchDoc(id)
      stored mustBe defined
      (stored.get \ "gameDefinitionId").as[String] mustBe gameDefinitionId
    }

    "return 422 when the gameDefinitionId does not exist" in {
      val body = Json.obj(
        "gameDefinitionId" -> java.util.UUID.randomUUID().toString
      )
      val request = FakeRequest(POST, "/games")
        .withJsonBody(body)
      val result = route(app, request).get

      status(result) mustBe UNPROCESSABLE_ENTITY
    }
  }

  "POST /users" should {
    "return 201 with the generated id and store the document" in {
      val body = Json.obj(
        "name" -> "Alice",
        "icon" -> "avatar_1"
      )
      val request = FakeRequest(POST, "/users")
        .withJsonBody(body)
      val result = route(app, request).get

      status(result) mustBe CREATED
      contentType(result) mustBe Some("application/json")
      val id = (contentAsJson(result) \ "id").as[String]

      val stored = fetchDoc(id)
      stored mustBe defined
      (stored.get \ "name").as[String] mustBe "Alice"
      (stored.get \ "icon").as[String] mustBe "avatar_1"
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

  private def fetchDoc(id: String): Option[JsObject] = {
    val config = app.configuration
    val response = await(
      inject[WSClient]
        .url(s"${config.get[String]("couchdb.url")}/${config.get[String]("couchdb.database")}/$id")
        .withAuth(config.get[String]("couchdb.username"), config.get[String]("couchdb.password"), WSAuthScheme.BASIC)
        .get()
    )
    if response.status == 200 then Some(response.json.as[JsObject]) else None
  }

  private def createGameDefinition(): String = {
    val request = FakeRequest(POST, "/game-definitions").withJsonBody(gameDefinitionBody)
    val result = route(app, request).get
    status(result) mustBe CREATED
    (contentAsJson(result) \ "id").as[String]
  }

  private def gameDefinitionBody = {
    val step = Json.obj(
      "clue" -> "Find the red door",
      "solution" -> "red_door",
      "hint" -> "Look near the entrance",
      "hintIcon" -> "door_icon",
      "inputType" -> "QR_SCAN",
      "location" -> Json.obj("longitude" -> -0.1276, "latitude" -> 51.5074)
    )
    Json.obj(
      "title" -> "Test Hunt",
      "definition" -> "A test scavenger hunt",
      "gameType" -> "TREASURE_HUNT",
      "steps" -> Json.arr(step)
    )
  }
}
