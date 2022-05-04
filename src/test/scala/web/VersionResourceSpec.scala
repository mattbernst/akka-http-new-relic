package web

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpec, Matchers}

class VersionResourceSpec extends FlatSpec with Matchers with ScalatestRouteTest with VersionResource {

  override def appVersion = "some-test-version"

  behavior of "versionRoute"

  it should "return appVersion for a get request" in {
    Get("/version") ~> versionRoute ~> check {
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe appVersion
    }
  }
}
