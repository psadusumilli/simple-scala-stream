import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.util.ByteString

/**
  * Created by padusumilli on 4/1/2016.
  */


object TCPClient {

  def props(remote: InetSocketAddress, replies: ActorRef): Props = {
    Props(classOf[TCPClient], remote, replies)
  }

}

class TCPClient(remote: InetSocketAddress, listener: ActorRef) extends Actor {

  import context.system

  IO(Tcp) ! Connect(remote)

  override def receive: Receive = {

    case CommandFailed(_: Connect) => {
      listener ! "connect failed"
      context stop self
    }

    case c @ Connected(remote, local) => {
      listener ! c

      val connection = sender()
      connection ! Register(self)

      context become  {
        case data: ByteString =>
          connection ! Write(data)


        case CommandFailed(w: Write) =>
          listener ! "write failed"

        case Received(data) =>
          listener ! data

        case "close" =>
          connection ! Close

        case _: ConnectionClosed =>
          listener ! "connection closed"
          context stop self

        case _ =>
          listener ! "unknown message format"


      }
    }
  }
}
