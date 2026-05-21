// Credentials for Artifact Registry (repository configured in project/repositories)
credentials ++= {
  val username = "_json_key_base64"
  val password = sys.env.getOrElse("ARTIFACT_REGISTRY_SA_KEY", "")
  if (password.nonEmpty) {
    Seq(Credentials("Artifact Registry", "europe-west1-maven.pkg.dev", username, password))
  } else {
    Seq.empty
  }
}

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.0")
