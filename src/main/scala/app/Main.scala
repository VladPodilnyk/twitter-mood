package app

import cats.effect.ExitCode
import monix.bio.{BIOApp, UIO}

object Main extends BIOApp {
  override def run(args: List[String]): UIO[ExitCode] = ???
}

object testApp extends App {
  import twitter4j.Twitter
  import twitter4j.TwitterFactory
  import twitter4j.conf.ConfigurationBuilder

  val cb = new ConfigurationBuilder
  cb.setDebugEnabled(true)
    .setOAuthConsumerKey("*********************")
    .setOAuthConsumerSecret("******************************************")
    .setOAuthAccessToken("**************************************************")
    .setOAuthAccessTokenSecret("******************************************")
  val tf      = new TwitterFactory(cb.build)
  val twitter = tf.getInstance
}
