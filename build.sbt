name := "SimpleStreams"

version := "1.0"

scalaVersion := "2.11.8"

fork in Test := true

libraryDependencies ++= Seq(
  "com.github.etaty" %% "rediscala" % "1.6.0",
  "com.typesafe.akka" %% "akka-actor" % "2.4.3",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % "2.0.4",
  "com.typesafe.akka" % "akka-http-core-experimental_2.11" % "2.0.4",
  "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.4.3",
  "com.typesafe.akka" % "akka-agent_2.11" % "2.4.3",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.3"

//"com.typesafe.akka" % "akka-cluster_2.11" % "2.4.3",
//"com.typesafe.akka" % "akka-remote_2.11" % "2.4.3",


)
