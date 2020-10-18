inThisBuild(
  Seq(
    name := "twitter-mood",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.3",
  )
)

//lazy val root = project
//  .in(file("."))
//  .settings(
//    name := "twitter-mood"
//  )
//  .aggregate(backend)

lazy val backend = project
  .in(file("./backend"))
  .settings(
    name := "twitter-mood",
    scalacOptions += "-Ymacro-annotations",
    scalacOptions --= Seq("-Werror", "-Xfatal-warnings"),
    libraryDependencies ++= Seq(
      Deps.monix_bio,
      Deps.cats,
      Deps.catsEffect,
      Deps.circeCore,
      Deps.circeParse,
      Deps.log4cats,
      Deps.twitter4j,
      Deps.htt4sDSL,
      Deps.blazeServer,
      Deps.htt4sCirce,
      Deps.scalaCheck % Test,
      Deps.scalaTest % Test,
    ),
    addCompilerPlugin(Deps.kindProjector),
  )
