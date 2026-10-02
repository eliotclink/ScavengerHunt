package repositories

import domain.User
import formats.JsonFormats.given
import javax.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.libs.json.{JsObject, JsString, Json}
import play.api.libs.ws.WSClient
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(ws: WSClient, config: Configuration)(implicit ec: ExecutionContext):

  private val couchDbUrl = config.get[String]("couchdb.url")
  private val dbName     = config.get[String]("couchdb.database")

  def create(user: User): Future[Unit] =
    val doc = Json.toJson(user).as[JsObject] + ("_id" -> JsString(user.id.toString))
    ws.url(s"$couchDbUrl/$dbName/${user.id}")
      .put(doc)
      .map(_ => ())
