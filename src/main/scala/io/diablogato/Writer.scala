package io.diablogato

import scala.util.{Success, Failure}
import akka.NotUsed
import akka.stream._
import akka.stream.scaladsl._
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import java.nio.file.{ StandardOpenOption, Paths, Files }
import ExecutionContext.Implicits.global
import scala.collection.JavaConversions._
import com.google.cloud.bigquery.FieldValueList

case class Writer(
  fields: Seq[String],
  rowIterator: Iterator[FieldValueList],
  rawPath: String,
  countPath: String
) {
  implicit val system = ActorSystem("BigQueryAwsConverter")
  implicit val materializer = ActorMaterializer()

  def write: Unit = {
    writeHeaders
    writeValues
  }

  def writeHeaders: Unit = {
    val content = fields.mkString(",") + "\n"
    Files.write(Paths.get(rawPath), content.getBytes);
  }

  def lineSink: Sink[FieldValueList, Future[IOResult]] =
    Flow[FieldValueList].map { fieldValueList =>
      val row = fieldValueList.map(_.getStringValue).mkString(",")
      ByteString(row + "\n")
    }.toMat(
      FileIO.toPath(Paths.get(rawPath), Set(StandardOpenOption.APPEND))
    )(Keep.right)

  val termCount: Flow[FieldValueList, String, NotUsed] =
    Flow[FieldValueList].map{ fieldValueList =>
      fieldValueList.get("currentPath").getStringValue
    }

  def writeValues: Unit = {
    import Utils._
    val source = Source.fromIterator(() => rowIterator)
    val g = RunnableGraph.fromGraph(GraphDSL.create(sumSink) { implicit b => sink =>
      import GraphDSL.Implicits._

      val bcast = b.add(Broadcast[FieldValueList](2))
      source ~> bcast.in
      bcast.out(0) ~> lineSink
      bcast.out(1) ~> termCount ~> sink
      ClosedShape
    })
    g.run().onSuccess {
      case v => {
        Files.write(Paths.get(countPath), v.toList.sortWith(_._2 > _._2).map{ case (a, b) => s"$a,$b" }.mkString("\n").getBytes)
        system.terminate()
      }
    }
  }
}
