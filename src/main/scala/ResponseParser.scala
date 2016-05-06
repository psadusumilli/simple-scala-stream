import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, Props}
import akka.util.ByteString
import com.idms.csp.ctf.data.Message

/**
  * Created by padusumilli on 4/3/2016.
  */


object ResponseParser {
  def props(): Props = {
    Props(classOf[ResponseParser])
  }
}

/**
  * A parser actor, parses the messages received from the server, and
  * constructs the Response Message object, which would be transformed
  * by other actors like JSON converts.
  *
  *
  * Test that this object construction is complete on receiving the message.
  */
class ResponseParser extends Actor with ActorLogging{
  override def receive: Receive = {
    case msg: ByteString => {
      val msgObj = new Message(msg.toString())
    }

  }
}
