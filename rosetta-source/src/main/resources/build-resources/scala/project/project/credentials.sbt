// Resolvers/credentials for building the meta-build itself (i.e. resolving the
// addSbtPlugin dependency declared in project/plugins.sbt). sbt's build definition is
// recursive: project/*.sbt is itself a build whose own dependencies are resolved using
// settings from project/project/*.sbt. Without this file, the credentials configured in
// project/plugins.sbt only take effect for that project's own tasks, but NOT for resolving
// the plugin dependency declared in that very file - it needs to be defined one level down.
resolvers := Seq(
  "Artifact Registry Mirror" at "https://europe-west1-maven.pkg.dev/production-208613/maven-central/"
)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

externalResolvers := resolvers.value
