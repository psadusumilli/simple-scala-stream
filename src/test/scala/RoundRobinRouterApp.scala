import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

/**
  * Created by padusumilli on 4/2/2016.
  */
object RoundRobinRouterApp extends App {
  val system = ActorSystem("RoundRobinRouterApp")
  val actor = system.actorOf(Props[Master])

  Vector.fill(100) {
    actor ! Work()
  }
}

class Master extends Actor with ActorLogging {
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[Worker])
      context watch r

      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case w: Work =>
      router.route(w, sender())

    case Terminated(a) =>
      router = router.removeRoutee(a)
      val r = context.actorOf(Props[Worker])
      context watch r
      router.addRoutee(r)
  }
}

case class Work()

class Worker extends Actor with ActorLogging {

  override def receive: Receive = {
    case w: Work =>
      log.info(self.path.name)
    case _ =>
      log.info("Unknown message")
  }
}
