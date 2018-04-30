package io.diablogato

import play.api.libs.json._
import scala.util.{Try, Success, Failure}
import scala.io.Source

case class BqQueryConfig(
  sql: String,
  legacy: Boolean
)

object BqQueryConfig {
  def load: Either[String, BqQueryConfig] = {
    implicit val bqJsonConfigReads = Json.reads[BqQueryConfig]
    val configPath = "BqQuery.json"
    Try(Source.fromFile(configPath)) match {
      case Failure(s1) => Left(s"Failed, $s1")
      case Success(v) => {
        Try(Json.parse(v.getLines.mkString)) match {
          case Failure(s2) => Left(s"Failed, $s2")
          case Success(json) => {
            Json.fromJson(json) match {
              case JsError(s3) => Left(s"Failed, $s3")
              case JsSuccess(v, p) => Right(v)
            }
          }
        }
      }
    }
  }
}
