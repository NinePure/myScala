

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "myScala"
  )
val AkkaVersion = "2.6.19"
val Slf4jVersion = "2.15.0"
val LogbackVersion = "1.2.3"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
  "ch.qos.logback" % "logback-classic" % LogbackVersion,
  "org.scala-lang" % "scala-compiler" % "2.9.1",
  "org.scala-lang" % "scala-reflect" % "2.9.1",
  "org.scala-lang" % "scala-library" % "2.9.1",
  "org.apache.pdfbox" % "pdfbox" % "2.0.26"
)