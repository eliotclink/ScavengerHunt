package repositories

import domain.GameDefinition
import formats.JsonFormats.given
import javax.inject._
import play.api.Configuration
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.libs.ws.JsonBodyWritables._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GameRepository @Inject()(ws: WSClient, config: Configuration)(implicit ec: ExecutionContext):

  private val couchDbUrl = config.get[String]("couchdb.url")
  private val dbName     = config.get[String]("couchdb.database")

  def create(gameDefinition: GameDefinition): Future[Unit] =
    val doc = Json.toJson(gameDefinition).as[JsObject] + ("_id" -> JsString(gameDefinition.id.toString))
    ws.url(s"$couchDbUrl/$dbName/${gameDefinition.id}")
      .put(doc)
      .map(_ => ())
