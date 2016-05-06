version       := "0.1"

scalaVersion  := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  val sprayVersion = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"      % sprayVersion,
    "io.spray"            %%  "spray-routing"  % sprayVersion,
    "io.spray"            %%  "spray-testkit"  % sprayVersion  % "test",
    "io.spray"            %%  "spray-json"    % "1.3.2",
    "com.typesafe.akka"   %%  "akka-actor"     % akkaVersion,
    "com.typesafe.akka"   %%  "akka-testkit"   % akkaVersion   % "test",
    "org.specs2"          %%  "specs2-core"    % "2.3.11"      % "test",
    "com.typesafe.slick"  %%  "slick"          % "3.0.0",
    "org.scalatest"        %  "scalatest_2.11" % "2.2.1",
    "com.typesafe"         %  "config"         % "1.2.1",
    "org.postgresql"           %  "postgresql"     % "9.3-1100-jdbc4",
    "com.github.t3hnar"   %%  "scala-bcrypt"   % "2.4",
    "org.mindrot"          %  "jbcrypt"        % "0.3m",
    "com.typesafe.slick"  %%  "slick-codegen"  % "3.0.0" % "compile",
    "com.github.kikuomax" %% "spray-jwt"       % "0.0.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "org.slf4j" % "slf4j-api"       % "1.7.7"
  )
}

libraryDependencies ++= {
  val slickJodaMapperVersion = "2.0.0"
  Seq(
    "com.github.tototoshi" %% "slick-joda-mapper" % slickJodaMapperVersion,
    "joda-time" % "joda-time" % "2.7",
    "org.joda" % "joda-convert" % "1.7"
  )
}

libraryDependencies ~= { _.map(_.exclude("org.slf4j", "slf4j-log4j12")) }

slick <<= slickCodeGenTask

sourceGenerators in Compile <+= slickCodeGenTask

lazy val slick = TaskKey[Seq[File]]("gen-tables")
lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  val outputDir = (dir / "main/scala").getPath
  val username = "biggsix"
  val password = "guest"
  val url = "jdbc:postgresql://localhost:15432/biggsix"
  val jdbcDriver = "org.postgresql.Driver"
  val slickDriver = "slick.driver.PostgresDriver"
  val pkg = "dao"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, username, password), s.log))
  val fname = outputDir + "/dao/Tables.scala"
  Seq(file(fname))
}
