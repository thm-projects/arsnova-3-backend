package de.thm.arsnova

import de.thm.arsnova.auditor.BasicAuditorSimulation
import de.thm.arsnova.tutor.BasicTutorSimulation

import io.gatling.core.Predef._ // 2
import io.gatling.http.Predef._
import scala.concurrent.duration._

class Stresstest extends Simulation {
  println("starting webserver")
  WebServer.main(Array())

  val httpProtocol = http
    .baseURL("http://localhost:9000")
    .inferHtmlResources(BlackList(""".*\.css""", """.*\.js""", """.*\.ico"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .doNotTrackHeader("1")
    .userAgentHeader("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0")

  val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

  val uri1 = "http://localhost:9000/session/1"

  val auditorScn = scenario("Test").exec(
    BasicAuditorSimulation.joinSession.pause(3),
    BasicAuditorSimulation.getAllPrepQuestions.pause(3),
    BasicAuditorSimulation.answerToMCQuestion
  )

  val tutorScn = scenario("Basic Tutor").exec(
    BasicTutorSimulation.createSession.pause(3),
    BasicTutorSimulation.createQuestion
  )

  setUp(
    auditorScn.inject(rampUsers(1000) over (5 seconds)),
    tutorScn.inject(rampUsers(100) over (5 seconds))
  ).protocols(httpProtocol)
}