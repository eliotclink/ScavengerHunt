package formats

import domain.{GameDefinition, GameInputType, GameType, Location, Step, User}
import play.api.libs.json.{Format, JsError, JsString, JsSuccess, Json, Reads, Writes}

object JsonFormats:
  private def enumFormat[T](valueOf: String => T): Format[T] = Format(
    Reads[T](_.validate[String].flatMap(s =>
      scala.util.Try(valueOf(s)).fold(_ => JsError(s"Invalid value: $s"), JsSuccess(_))
    )),
    Writes[T](t => JsString(t.toString))
  )

  given Format[GameType]       = enumFormat(GameType.valueOf)
  given Format[GameInputType]  = enumFormat(GameInputType.valueOf)
  given Format[Location]       = Json.format[Location]
  given Format[GameDefinition] = Json.format[GameDefinition]
  given Format[Step]           = Json.format[Step]
  given Format[User]           = Json.format[User]

