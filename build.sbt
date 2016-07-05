organization := "com.olvind"
name := "mui-generator"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalaz"    %% "scalaz-core"    % "7.1.3",
  "io.argonaut"   %% "argonaut"       % "6.1",
  "com.lihaoyi"   %% "ammonite-ops"   % "0.6.2",
  "com.lihaoyi"   %% "fastparse"      % "0.3.7",
  "org.scalameta" %% "scalameta"      % "1.0.0",
  "org.scalatest" %% "scalatest"      % "3.0.0-M15" % Test
)

