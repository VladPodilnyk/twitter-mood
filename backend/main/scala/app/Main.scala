package app

import app.twitter.client.{TwitterClient, TwitterComponent}
import app.twitter.config.ForbiddenWordsLoader
import app.twitter.http.StatusRoutes
import cats.syntax.functor._
import cats.effect.ExitCode
import monix.bio.{BIOApp, Task, UIO}
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Main extends BIOApp {
  override def run(args: List[String]): UIO[ExitCode] = {
    for {
      component        <- TwitterComponent.mkComponent
      blackListedWords <- ForbiddenWordsLoader.load
      client           <- TwitterClient.mkClient(component, blackListedWords)
      httpApp          <- UIO.eval(new StatusRoutes(client))
      fiber            <- client.poll.start
      _ <- BlazeServerBuilder
        .apply[Task](global)
        .bindHttp(8080, "localhost")
        .withHttpApp(httpApp.routes)
        .serve
        .compile
        .drain
        .onErrorHandleWith(_ => fiber.join.void)
    } yield ()
    // TODO: add logger
  }.onErrorHandle(e => println(e)).flatMap(_ => UIO(ExitCode.Success))
}
