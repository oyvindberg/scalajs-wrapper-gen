organization := "com.olvind"
name := "mui-generator"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalaz"              %% "scalaz-core"       % "7.1.3",
  "io.argonaut"             %% "argonaut"          % "6.1-M4",
  "com.lihaoyi"             %% "ammonite-tools"    % "0.4.6",
  "com.lihaoyi"             %% "ammonite-ops"      % "0.4.6",
  "com.lihaoyi"             %% "fastparse"         % "0.3.4",
  "org.scalatest"           %% "scalatest"         % "3.0.0-M15" % Test
)

