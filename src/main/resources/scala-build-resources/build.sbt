import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val versions = new {
  val jackson = "2.10.0"
}

resolvers += Resolver.bintrayRepo("commodityvectors", "commodityvectors-releases")

lazy val root = (project in file("."))
  .settings(
    name := "cdm-example",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % versions.jackson,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % versions.jackson,
      "com.commodityvectors" %% "scalatest-snapshot-matcher-core" % "2.0.2"
    )
  )
