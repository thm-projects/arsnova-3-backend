name := "ARSnova-prototype"

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaVersion = "2.4.8"
  val scalaTestVersion = "3.0.0"
  val scalaMockVersion = "3.2.2"
  val slickVersion = "3.1.1"

  Seq(
    "com.typesafe.akka"     %% "akka-actor"                           % akkaVersion,
    "com.typesafe.akka"     %% "akka-stream"                          % akkaVersion,
    "com.typesafe.akka"     %% "akka-http-core"                       % akkaVersion,
    "com.typesafe.akka"     %% "akka-http-experimental"               % akkaVersion,
    "com.typesafe.akka"     %% "akka-http-spray-json-experimental"    % akkaVersion,
    "com.typesafe.akka"     %% "akka-http-testkit"                    % akkaVersion,
    "com.typesafe.slick"    %% "slick"                                % slickVersion,
    "com.typesafe.slick"    %% "slick-hikaricp"                       % slickVersion,
    "org.slf4j"             %  "slf4j-nop"                            % "1.7.21",
    "mysql"                 %  "mysql-connector-java"                 % "6.0.3",
    "org.flywaydb"          %  "flyway-core"                          % "3.2.1",
    "com.typesafe.akka"     %% "akka-testkit"                         % akkaVersion % "test",
    "org.scalatest"         %% "scalatest"                            % scalaTestVersion
//  "org.scalamock"         %% "scalamock-scalatest-support"          % scalaMockVersion
  )
}