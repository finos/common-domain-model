import sbt._

object Dependencies {
  // scalatest 3.0.8 predates Scala 3 support entirely (it was only published for Scala
  // 2.10/2.11/2.12/2.13), which is why "scalatest_3:3.0.8" could not be found on the proxy.
  // 3.2.19 is cross-published for Scala 3 (as "scalatest_3") and, thanks to Scala 3's TASTy
  // forward binary compatibility, works with any Scala 3.x compiler, including 3.8.4.
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.19"
}
