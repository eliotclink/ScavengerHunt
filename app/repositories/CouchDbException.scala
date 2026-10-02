package repositories

import play.api.libs.ws.WSResponse

case class CouchDbException(status: Int, body: String)
  extends Exception(s"CouchDB request failed with status $status: $body")

object CouchDbException:

  def requireSuccess(response: WSResponse): Unit =
    if response.status < 200 || response.status >= 300 then
      throw CouchDbException(response.status, response.body)
