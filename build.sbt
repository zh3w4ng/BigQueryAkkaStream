name := """bigquery-akka-loader"""

version := "1.0"

scalaVersion := "2.12.4"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.12" % Test,
  "com.google.cloud" % "google-cloud-bigquery" % "1.27.0",
  "com.typesafe.play" %% "play-json" % "2.6.7"
)

assemblyJarName in assembly := "loader.jar"
mainClass in assembly := Some("io.diablogato.Main")
test in assembly := {}
