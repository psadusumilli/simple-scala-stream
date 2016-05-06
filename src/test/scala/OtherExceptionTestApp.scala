import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

/**
  * Created by padusumilli on 4/2/2016.
  */
object OtherExceptionTestApp extends App {
  val system = ActorSystem("OtherExceptionTest")
  val actor = system.actorOf(Props[OtherExceptionParent])

  actor ! "create child"
}

class OtherExceptionTest {

}

class OtherExceptionParent extends Actor with ActorLogging {

  override def receive: Receive = {
    case "create child" => {
      log.info("creating child")
      val child = context.actorOf(Props[OtherExceptionChild])

      child ! "throwSomeException"
      child ! "someMessage"
    }

  }
}

class OtherExceptionChild extends Actor with ActorLogging {

  override def preStart = {
    log.info("Starting Child Actor")
  }

  override def receive: Receive = {
    case "someMessage" =>
      log.info("Restarted and printing some Message")

    case "throwSomeException" =>
      new Exception("Some exception")

  }

  override def postStop = {
    log.info("Stopping child actor.")
  }
}
