name := """play-java-rest-api-example"""

version := "2.6.x"

def gatlingVersion(scalaBinVer: String): String = scalaBinVer match {
  case "2.11" => "2.2.5"
  case "2.12" => "2.3.1"
}

inThisBuild(
  List(
    crossScalaVersions := Seq("2.11.12", "2.12.8"),
    scalaVersion := "2.12.8"
  )
)


lazy val GatlingTest = config("gatling") extend Test

lazy val root = (project in file(".")).enablePlugins(PlayJava, GatlingPlugin).configs(GatlingTest)
  .settings(inConfig(GatlingTest)(Defaults.testSettings): _*)
  .settings(
    scalaSource in GatlingTest := baseDirectory.value / "/gatling/simulation"
  )

libraryDependencies += guice
libraryDependencies += javaJpa
libraryDependencies += "com.h2database" % "h2" % "1.4.197"

libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.17.Final"
libraryDependencies += "io.dropwizard.metrics" % "metrics-core" % "3.2.1"
libraryDependencies += "com.palominolabs.http" % "url-builder" % "1.1.0"
libraryDependencies += "net.jodah" % "failsafe" % "1.0.3"

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion(scalaBinaryVersion.value) % Test
libraryDependencies += "io.gatling" % "gatling-test-framework" % gatlingVersion(scalaBinaryVersion.value) % Test


PlayKeys.externalizeResources := false

testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
