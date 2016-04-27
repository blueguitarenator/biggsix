version       := "0.1"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion = "2.3.6"
  val sprayVersion = "1.3.2"
  Seq(
    "io.spray"            %%  "spray-can"      % sprayVersion,
    "io.spray"            %%  "spray-routing"  % sprayVersion,
    "io.spray"            %%  "spray-json"     % sprayVersion,
    "io.spray"            %%  "spray-testkit"  % sprayVersion  % "test",
    "com.typesafe.akka"   %%  "akka-actor"     % akkaVersion,
    "com.typesafe.akka"   %%  "akka-testkit"   % akkaVersion   % "test",
    "org.specs2"          %%  "specs2-core"    % "2.3.11"      % "test",
    "com.typesafe.slick"  %%  "slick"          % "3.0.0",
    "org.slf4j"            % "slf4j-nop"       % "1.7.7",
    "org.slf4j"            % "slf4j-api"       % "1.7.7",
    "org.scalatest"        %  "scalatest_2.11" % "2.2.1",
    "com.typesafe"         %  "config"         % "1.2.1",
    "postgresql"           %  "postgresql"     % "9.1-901.jdbc4",
    "com.h2database"       %  "h2"             % "1.4.182",
    "com.gettyimages"     %%  "spray-swagger"  % "0.5.0",
    "org.webjars"          %  "swagger-ui"     % "2.0.12",
    "com.github.t3hnar"   %%  "scala-bcrypt"   % "2.4",
    "org.mindrot"          %  "jbcrypt"        % "0.3m",
    "com.typesafe.slick"  %%  "slick-codegen" % "3.0.0" % "compile"
  )
}

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
