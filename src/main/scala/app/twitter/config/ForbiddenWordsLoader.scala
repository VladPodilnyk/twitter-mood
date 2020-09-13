package app.twitter.config

import app.twitter.domain.ForbiddenWords
import io.circe.parser.decode
import monix.bio.{Task, UIO}

import scala.io.Source

object ForbiddenWordsLoader {
  private val filename = "blacklisted-words.json"
  def load: Task[ForbiddenWords] = {
    Task.eval(Source.fromResource(filename))
      .bracket(buf => decode[List[String]](buf.mkString).toTask.map(ForbiddenWords.apply))(buf => UIO(buf.close()))
  }

  private implicit class FromEitherToTaskConversion(value: Either[io.circe.Error, List[String]]) {
    def toTask: Task[List[String]] = value match {
      case Left(error)  => Task.terminate(new Throwable(s"Couldn't parse data from $filename due to ${error.getMessage}"))
      case Right(value) => Task.pure(value)
    }
  }
}
