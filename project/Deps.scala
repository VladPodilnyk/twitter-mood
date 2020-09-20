import sbt._

object Deps {
  final val monix_bio  = "io.monix" %% "monix-bio" % Versions.monix_bio
  final val cats       = "org.typelevel" %% "cats-core" % Versions.cats
  final val catsEffect = "org.typelevel" %% "cats-effect" % Versions.cats_effect
  final val catsRetry  = "com.github.cb372" %% "cats-retry" % Versions.cats_retry
  final val circeCore  = "io.circe" %% "circe-core" % Versions.circe
  final val circeParse = "io.circe" %% "circe-parser" % Versions.circe
  final val log4cats   = "io.chrisdavenport" %% "log4cats-slf4j" % Versions.log4cats
  final val twitter4j  = "org.twitter4j" % "twitter4j-core" % Versions.twitter4j

  final val htt4sDSL   = "org.http4s" %% "http4s-dsl" % Versions.htt4s
  final val blazeServer = "org.http4s" %% "http4s-blaze-server" % Versions.htt4s


  // Compiler plugins
  final val kindProjector = "org.typelevel" % "kind-projector" % Versions.kind_projector cross CrossVersion.full

  // Tests
  val scalaCheck = "org.scalacheck" %% "scalacheck" % Versions.scalaCheck
  val scalaTest  = "org.scalatest" %% "scalatest" % Versions.scalaTest
}
