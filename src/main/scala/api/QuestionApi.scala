package api

import services.QuestionService

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models._
import akka.http.scaladsl.server.Directives._
import spray.json._

import hateoas.{ApiRoutes, ResourceAdapter, Link}

trait QuestionApi {
  import mappings.QuestionJsonProtocol._

  val questionCrudPoint = "question"

  def questionSelfLink(question: Question): Link = {
    Link("self", s"/$questionCrudPoint/${question.id.get}")
  }

  val questionAdapter = new ResourceAdapter[Question](questionFormat, questionSelfLink)

  val questionApi = pathPrefix(questionCrudPoint) {
    pathEndOrSingleSlash {
      get {
        parameters("sessionid".as[SessionId], "variant".?) { (sessionId, variant) =>
          variant match {
            case Some(v) => complete(QuestionService.findQuestionsBySessionIdAndVariant(sessionId, v)
              .map(questionAdapter.toResources(_)))
            case None => complete {QuestionService.findAllBySessionId(sessionId)
              .map(questionAdapter.toResources(_))}
          }
        }
      } ~
      post {
        entity(as[Question]) { question =>
          complete (QuestionService.create(question).map(_.toJson))
        }
      }
    } ~
    pathPrefix(IntNumber) { id =>
      pathEndOrSingleSlash {
        get {
          complete (QuestionService.findById(id).map(questionAdapter.toResource(_)))
        } ~
        put {
          entity(as[Question]) { question =>
            complete (QuestionService.update(question, id).map(_.toJson))
          }
        } ~
        delete {
          complete (QuestionService.delete(id).map(_.toJson))
        }
      }
    }
  }
}
