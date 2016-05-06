package demo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

/**
 * Created by padusumilli on 3/18/2016.
*/
object DemoStreams extends App {
  implicit val system = ActorSystem("demo-actors")
  implicit val mat = ActorMaterializer()

  val source = Source(1 to 10)
  val sink = Sink.fold[Int, Int] (0)(_ + _)

  source.to(sink)


}
