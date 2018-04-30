package io.diablogato
import scala.concurrent._
import akka.stream.scaladsl._

object Utils {
  val sumSink: Sink[String, Future[Map[String, Int]]] =
    Sink.fold[Map[String, Int], String](Map[String, Int]())((acc, a) => acc + (a -> (acc.get(a).getOrElse(0) + 1)))
}
