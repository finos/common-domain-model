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

    description := "The FINOS Common Domain Model (CDM) is a standardised, machine-readable and machine-executable blueprint for how financial products are traded and managed across the transaction lifecycle. It is represented as a domain model and distributed in open source.",

    licenses += "Community Specification License 1.0" -> url("https://github.com/finos/common-domain-model/blob/master/LICENSE.md"),

    libraryDependencies ++= Seq(
      scalaTest % Test,
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % versions.jackson,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % versions.jackson
    )
  )
