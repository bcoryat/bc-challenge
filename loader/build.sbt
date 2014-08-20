name := "loader"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.mongodb" %% "casbah" % "2.7.3",
  "org.skife.com.typesafe.config" % "typesafe-config" % "0.3.0"
)