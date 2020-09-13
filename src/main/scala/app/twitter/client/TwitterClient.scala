package app.twitter.client

import app.twitter.domain.ForbiddenWords
import monix.bio.{Cause, Task, UIO}
import twitter4j.{Query, QueryResult}

import scala.concurrent.duration._
import scala.jdk.CollectionConverters._

trait TwitterClient {
  def poll: UIO[Unit]
}

object TwitterClient {
  def mkClient(component: TwitterComponent, blackListedWords: ForbiddenWords): Task[TwitterClient] = Task.eval(new Impl(component, new TextProcessor(blackListedWords)))

  private final class Impl(twitter: TwitterComponent, textProcessor: TextProcessor) extends TwitterClient {
    private val maxTweetsPerTrend = 500
    private val tweetLanguage     = "en"
    private val maxTweetsInBatch  = 100

    override def poll: UIO[Unit] = {
      (for {
        trends <- fetchTrends
        res <-
          Task
            .parTraverse(trends) {
              t =>
                val query = new Query(t).lang(tweetLanguage).count(maxTweetsInBatch)
                collectTweets(query)
            }.map(_.flatten)

        mood = textProcessor.defineMood(res)
        _   <- UIO(println(mood))
        _   <- Task.sleep(2.minutes)
      } yield ()).redeemCause(logError, _ => UIO.unit).loopForever
    }

    private[this] def fetchTrends: Task[List[String]] = {
      val regex = "#[a-z|A-Z|0-9]*".r
      for {
        trends        <- twitter.request(_.trends().getPlaceTrends(1))
        engNamedTrends = trends.getTrends.toList.filter(t => regex.matches(t.getName)).map(_.getName)
      } yield engNamedTrends
    }

    private[this] def collectTweets(query: Query): Task[List[String]] = {
      def loop(query: QueryResult, acc: List[String]): Task[List[String]] = {
        if (query.hasNext && acc.size < maxTweetsPerTrend)
          twitter.request(_.search(query.nextQuery())).flatMap(res => loop(res, acc ++ query.getTweets.asScala.map(_.getText)))
        else Task.pure(acc)
      }

      for {
        firstBatch <- twitter.request(_.search(query))
        res        <- loop(firstBatch, firstBatch.getTweets.asScala.map(_.getText).toList)
      } yield res
    }

    // TODO: add logger
    private[this] def logError(cause: Cause[Throwable]): UIO[Unit] = {
      cause match {
        case Cause.Error(value)       => UIO(println(s"Catch an error while polling tweets: ${value.getMessage}"))
        case Cause.Termination(value) => UIO(println(s"Terminated with ${value.getMessage}"))
      }
    }

  }
}
