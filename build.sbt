ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.8"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "ScavengerHunt",
    Test / javaOptions += "-Dconfig.file=conf/test.conf",
    Test / fork := true,
    libraryDependencies ++= Seq(
      guice,
      ws,
      "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
    )
  )
