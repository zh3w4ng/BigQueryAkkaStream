package io.diablogato

import play.api.libs.json._

trait Globals {
  type OutputFilePath = String
  type BqQueryConfig = JsValue
}
