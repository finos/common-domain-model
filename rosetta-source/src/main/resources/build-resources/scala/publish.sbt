ThisBuild / organization := "org.finos.cdm"
ThisBuild / organizationName := "cdm-scala"
ThisBuild / organizationHomepage := Some(url("https://www.finos.org/common-domain-model"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/finos/common-domain-model"),
    "scm:git:git://github.com/finos/common-domain-model.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "minesh-s-patel",
    name = "Minesh Patel",
    email = "infra@regnosys.com",
    url = url("http://github.com/minesh-s-patel")
  ),
  Developer(
      id = "hugohills-regnosys",
      name = "Hugo Hills",
      email = "infra@regnosys.com",
      url = url("http://github.com/hugohills-regnosys")
  )
)

ThisBuild / description := The FINOS Common Domain Model (CDM) is a standardised, machine-readable and machine-executable blueprint for how financial products are traded and managed across the transaction lifecycle. It is represented as a domain model and distributed in open source."
ThisBuild / licenses := List(
  licenses += "Community Specification License 1.0" -> url("https://github.com/finos/common-domain-model/blob/master/LICENSE.md")
)
ThisBuild / homepage := Some(url("https://www.finos.org/common-domain-model"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true