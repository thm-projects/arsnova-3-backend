package de.thm.arsnova

import de.thm.arsnova.services.{BaseService, FeaturesService}
import de.thm.arsnova.models._
import de.thm.arsnova.api.FeaturesApi

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.model.StatusCodes._

trait FeaturesApiSpec extends FunSpec with Matchers with ScalaFutures with BaseService with ScalatestRouteTest with Routes
    with TestData with FeaturesApi {
  import de.thm.arsnova.mappings.FeatureJsonProtocol._
  describe("Features api") {
    it("retrieve features by id") {
      Get("/features/1") ~> featuresApi ~> check {
        responseAs[JsObject] should be(featuresAdapter.toResource(testFeatures.head))
      }
    }
    it("retrieve features for session 1") {
      Get("/session/1/features") ~> featuresApi ~> check {
        responseAs[JsObject] should be(featuresAdapter.toResource(testFeatures.head))
      }
    }
    it("create features") {
      val postFeature = Features(None, 4, true, true, false, false, true, true, false, false, true, true)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, postFeature.toJson.toString)
      Post("/features/", requestEntity) ~> featuresApi ~> check {
        response.status should be(OK)
      }
    }
    it("update features") {
      val feature = testFeatures.head
      val putFeature = feature.copy(flashcards = false, jitt = false)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, putFeature.toJson.toString)
      Put("/features/" + putFeature.id.get, requestEntity) ~> featuresApi ~> check {
        response.status should be(OK)
        Get("/features/" + putFeature.id.get) ~> featuresApi ~> check {
          responseAs[JsObject] should be(featuresAdapter.toResource(putFeature))
        }
      }
    }
    it("delete features") {
      val featureId = testFeatures.head.id.get
      Delete("/features/" + featureId) ~> featuresApi ~> check {
        response.status should be(OK)
        Get("/features/" + featureId) ~> featuresApi ~> check {
          response.status should be(NotFound)
        }
      }
    }
  }
}
