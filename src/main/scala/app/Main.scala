package app

import app.twitter.client.TwitterComponent
import cats.effect.ExitCode
import monix.bio.{BIOApp, Task, UIO}

import scala.jdk.CollectionConverters._

object Main extends BIOApp {
  override def run(args: List[String]): UIO[ExitCode] = {
    TwitterComponent.mkComponent.use {
      c =>
        for {
          rawTrends <- c.request(_.trends().getAvailableTrends)
          _         <- Task(println(rawTrends.asScala.toList.map(_.getName)))
        } yield ()
    }
  }.onErrorHandle(e => println(e)).flatMap(_ => UIO(ExitCode.Success))
}