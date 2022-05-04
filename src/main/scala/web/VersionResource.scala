package web

trait VersionResource {

  def appVersion: String

  import akka.http.scaladsl.server.Directives._
  import akka.http.scaladsl.server.Route

  lazy val versionRoute: Route = path("version") {
    get {
      complete(appVersion)
    }
  }
}
