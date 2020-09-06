package app

import app.twitter.client.{TwitterClient, TwitterComponent}
import cats.effect.ExitCode
import monix.bio.{BIOApp, UIO}

object Main extends BIOApp {
  override def run(args: List[String]): UIO[ExitCode] = {
    for {
      component <- TwitterComponent.mkComponent
      client    <- TwitterClient.mkClient(component)
      _         <- client.poll
    } yield ()
    // TODO: add logger
  }.onErrorHandle(e => println(e)).flatMap(_ => UIO(ExitCode.Success))
}
