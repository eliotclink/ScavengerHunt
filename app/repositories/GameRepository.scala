package repositories

import domain.{Game, GameDefinition}
import formats.JsonFormats.given
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.libs.json.{JsObject, JsString, Json}
import play.api.libs.ws.{WSAuthScheme, WSClient, WSRequest}
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GameRepository @Inject()(ws: WSClient, config: Configuration)(implicit ec: ExecutionContext):

  private val couchDbUrl = config.get[String]("couchdb.url")
  private val dbName     = config.get[String]("couchdb.database")
  private val username   = config.get[String]("couchdb.username")
  private val password   = config.get[String]("couchdb.password")

  private def docRequest(id: String): WSRequest =
    ws.url(s"$couchDbUrl/$dbName/$id").withAuth(username, password, WSAuthScheme.BASIC)

  def create(gameDefinition: GameDefinition): Future[Unit] =
    val doc = Json.toJson(gameDefinition).as[JsObject] + ("_id" -> JsString(gameDefinition.id.toString))
    docRequest(gameDefinition.id.toString)
      .put(doc)
      .map(CouchDbException.requireSuccess)

  def create(game: Game): Future[Unit] =
    val doc = Json.toJson(game).as[JsObject] + ("_id" -> JsString(game.id.toString))
    docRequest(game.id.toString)
      .put(doc)
      .map(CouchDbException.requireSuccess)

  def get(gameDefinitionId: String): Future[Option[GameDefinition]] =
    docRequest(gameDefinitionId)
      .get()
      .map { response =>
        if response.status == 404 then None
        else
          CouchDbException.requireSuccess(response)
          Some(response.json.as[GameDefinition])
      }
