// Maven Central mirror configuration for plugin resolution
resolvers := Seq(
  "Maven Central Proxy" at "https://europe-west1-maven.pkg.dev/production-208613/maven-central"
)

credentials ++= {
  val username = "_json_key_base64"
  val password = sys.env.getOrElse("ARTIFACT_REGISTRY_SA_KEY", "")
  if (password.nonEmpty) {
    Seq(Credentials("Artifact Registry", "europe-west1-maven.pkg.dev", username, password))
  } else {
    Seq.empty
  }
}

// Force all plugin resolution through our proxy only
externalResolvers := Resolver.combineDefaultResolvers(resolvers.value.toVector, mavenCentral = false)

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.0")
