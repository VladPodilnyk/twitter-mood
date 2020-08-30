package app.twitter.client

import cats.effect.Resource
import monix.bio.{IO, Task}
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Twitter, TwitterFactory}

import scala.util.chaining._

trait TwitterComponent[F[_, _]] {
  def request[A](f: Twitter => A): F[Throwable, A]
}

object TwitterComponent {
  def mkComponent: Resource[Task, TwitterComponent[IO]] = {
    Resource.make {
      IO.eval {
        for {
          oauthConsumerKey       <- sys.env.get("twitterOauthConsumerKey")
          oauthConsumerSecret    <- sys.env.get("twitterOauthConsumerSecret")
          oauthAccessToken       <- sys.env.get("twitterOauthAccessToken")
          oauthAccessTokenSecret <- sys.env.get("twitterOauthAccessTokenSecret")
        } yield {
          val configuration = new ConfigurationBuilder()
            .pipe(_.setDebugEnabled(true))
            .pipe(_.setOAuthConsumerKey(oauthConsumerKey))
            .pipe(_.setOAuthConsumerSecret(oauthConsumerSecret))
            .pipe(_.setOAuthAccessToken(oauthAccessToken))
            .pipe(_.setOAuthAccessTokenSecret(oauthAccessTokenSecret))
            .build()
          val component = new TwitterFactory(configuration).getInstance()
          new Impl(component)
        }
      }.flatMap(_.map(Task.pure).getOrElse(Task.terminate(EnvVariablesAreNotSet)))
    } {
      _ => Task.unit
    }
  }

  private final class Impl(client: Twitter) extends TwitterComponent[IO] {
    override def request[A](f: Twitter => A): Task[A] = IO.eval(f(client))
  }

  // FIXME: THIS IS TEMPORARY!
  final val EnvVariablesAreNotSet = new Throwable("You forgot to set credentials as env variables")
}
