import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.0.0.snapshot"
ThisBuild / organization     := "org.finos.cdm"
ThisBuild / organizationName := "cdm-scala"

// Maven Central mirror configuration
ThisBuild / resolvers := Seq(
  "Maven Central Proxy" at "https://europe-west1-maven.pkg.dev/production-208613/maven-central"
)

ThisBuild / credentials ++= {
  val username = "_json_key_base64"
  val password = sys.env.getOrElse("ARTIFACT_REGISTRY_SA_KEY", "")
  if (password.nonEmpty) {
    Seq(Credentials("Artifact Registry", "europe-west1-maven.pkg.dev", username, password))
  } else {
    Seq.empty
  }
}

ThisBuild / externalResolvers := resolvers.value

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