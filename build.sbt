inThisBuild(
  Seq(
    name := "twitter-mood",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.3",
  )
)

lazy val app = project
  .in(file("."))
  .settings(
    name := "twitter-mood",
    scalacOptions += "-Ymacro-annotations",
    scalacOptions --= Seq("-Werror", "-Xfatal-warnings"),
    libraryDependencies ++= Seq(
        Deps.monix_bio,
        Deps.cats,
        Deps.catsEffect,
        Deps.catsRetry,
        Deps.circeCore,
        Deps.circeParse,
        Deps.log4cats,
        Deps.twitter4j,
        Deps.scalaCheck % Test,
        Deps.scalaTest % Test,
      ),
    addCompilerPlugin(Deps.kindProjector),
  )
