// Configure resolver to use Google Artifact Registry mirror for plugins
resolvers := Seq(
  "Artifact Registry Mirror" at "https://europe-west1-maven.pkg.dev/production-208613/maven-central/"
)

// Credentials for Artifact Registry
credentials ++= {
  val username = "_json_key_base64"
  val password = sys.env.getOrElse("ARTIFACT_REGISTRY_SA_KEY", "")
  if (password.nonEmpty) {
    Seq(Credentials("Artifact Registry", "europe-west1-maven.pkg.dev", username, password))
  } else {
    Seq.empty
  }
}

// Tell Coursier to use our configured resolvers
externalResolvers := resolvers.value

// Configure Coursier to use credentials
import coursier.credentials.DirectCredentials
csrConfiguration := {
  val creds = sys.env.get("ARTIFACT_REGISTRY_SA_KEY").map { password =>
    DirectCredentials()
      .withHost("europe-west1-maven.pkg.dev")
      .withUsername("_json_key_base64")
      .withPassword(password)
  }
  csrConfiguration.value.addCredentials(creds.toSeq: _*)
}

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.0")
