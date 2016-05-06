package redis

/**
  * Created by padusumilli on 4/26/2016.
  */


import java.net.InetSocketAddress

import akka.actor.Props
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{Message, PMessage}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object PubSubRedis extends App {
  implicit val akkaSystem = akka.actor.ActorSystem()

  val redis = RedisClient()

  // publish after 2 seconds every 2 or 5 seconds
  akkaSystem.scheduler.schedule(2 seconds, 2 seconds)(redis.publish("time", System.currentTimeMillis()))
  akkaSystem.scheduler.schedule(2 seconds, 5 seconds)(redis.publish("pattern.match", "pattern value"))
  // shutdown Akka in 20 seconds
  akkaSystem.scheduler.scheduleOnce(20 seconds)(akkaSystem.shutdown())

  val channels = Seq("time")
  val patterns = Seq("pattern.*")
  // create SubscribeActor instance
  akkaSystem.actorOf(Props(classOf[SubscribeActor], channels, patterns).withDispatcher("rediscala.rediscala-client-worker-dispatcher"))

}

class SubscribeActor(channels: Seq[String] = Nil, patterns: Seq[String] = Nil)
  extends RedisSubscriberActor(
    new InetSocketAddress("localhost", 6379),
    channels,
    patterns,
    onConnectStatus = connected => { println(s"connected: $connected")}
  ) {
  override val address: InetSocketAddress = new InetSocketAddress("localhost", 6379)

  def onMessage(message: Message) {
    println(s"message received: $message")
  }

  def onPMessage(pmessage: PMessage) {
    println(s"pattern message received: $pmessage")
  }
}

class PubSubRedisExp {

}
