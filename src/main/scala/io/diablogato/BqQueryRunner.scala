package io.diablogato

import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.FieldValueList
import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.QueryJobConfiguration
import scala.collection.JavaConversions._

case class BqQueryRunner(
  config: BqQueryConfig
) extends Globals {
  val bq: BigQuery = BigQueryOptions.getDefaultInstance.getService

  def run: (Seq[String], Iterator[FieldValueList]) = {
    val queryConfig = QueryJobConfiguration.newBuilder(config.sql).setUseLegacySql(config.legacy).build
    val queryResult = bq.query(queryConfig)
    val fields = queryResult.getSchema.getFields.map(_.getName).toSeq
    val rowIterator = queryResult.iterateAll.iterator
    (fields, rowIterator)
  }


}
