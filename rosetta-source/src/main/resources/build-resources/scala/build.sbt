import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.0.0.snapshot"
ThisBuild / organization     := "org.finos.cdm"
ThisBuild / organizationName := "cdm-scala"

val versions = new {
  val jackson = "2.10.0"
}

lazy val root = (project in file("."))
  .settings(
    name := "cdm-scala",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % versions.jackson,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % versions.jackson
    )
  )
