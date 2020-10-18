package app.twitter.http

import app.twitter.client.TwitterClient
import org.http4s.dsl.Http4sDsl
import monix.bio.Task
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.implicits._
import org.http4s.server.middleware._
import io.circe.syntax._
import org.http4s.circe._

final class StatusRoutes(twitterClient: TwitterClient) extends Http4sDsl[Task] {

  private val httpRoutes = HttpRoutes.of[Task] {
    case GET -> Root =>
      for {
        status <- twitterClient.get
        response <- Ok(status.asJson)
      } yield response
  }

  val routes = CORS(Router("/status" -> httpRoutes)).orNotFound
}
