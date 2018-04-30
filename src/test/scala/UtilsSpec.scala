import org.scalatest._
import io.diablogato.Utils._
import akka.stream._
import akka.stream.scaladsl._
import akka.actor.ActorSystem
import akka.stream.testkit._
import akka.stream.testkit.scaladsl._
import scala.concurrent.duration._
import scala.concurrent._

class WriterSpec extends FlatSpec with Matchers {
  implicit val system = ActorSystem("Test")
  implicit val materializer = ActorMaterializer()

  "sumsink" should "sum counts for each visited page" in {
    val sinkUnderTest = sumSink

    val future = Source(List("a", "b", "a", "c")).runWith(sinkUnderTest)
    val result = Await.result(future, 3.seconds)
    result should be (Map("a" -> 2, "b" -> 1, "c" -> 1))
  }
}
