import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

/**
  * Created by padusumilli on 4/2/2016.
  */
object OneForOneStrategyTestApp extends App {
  val system = ActorSystem("OneForOneStrategyTestApp")
  val actor = system.actorOf(Props[OneForOneStrategyParent], "OneForOneStrategyParent")

  actor ! "create child"
}

class OneForOneStrategyParent extends Actor with ActorLogging {
  override def receive: Receive = {
    case "create child" => {
      val child = context.actorOf(Props[OneForOneStratageyChild], "OneForOneStratageyChild")

    }
  }

}

class OneForOneStratageyChild extends Actor with ActorLogging {
  override def preStart = {

  }

  override def postStop = {

  }


  override def receive: Receive = {
    case _ =>
      log.info("Do nothing...")

  }

}
