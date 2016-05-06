import java.io.{File, FileOutputStream}
import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.Tcp.Connected
import akka.util.ByteString
import com.idms.csp.Utils

/**
  * Created by padusumilli on 4/1/2016.
  */

object CSPClientApp extends App {

  val system = ActorSystem("csp-client")

  val serverIp = new InetSocketAddress("172.29.20.12", 7022)
  val username = "idmsuser40"
  val password = "idmsuser40"

  val conn = Connection(serverIp, username, password)


  val pw: FileOutputStream = new FileOutputStream(new File("response.dat"))
  val cspRef = system.actorOf(CSPConnection.props(conn, pw), "csp")

  cspRef ! Command("login", conn)

}


case class Connection(serverAddress: InetSocketAddress, username: String, password: String)

case class Command(command: String, connection: Connection)


object CSPConnection {

  def props(conn: Connection, fileWriter: FileOutputStream): Props = {
    Props(classOf[CSPConnection], conn, fileWriter)
  }

}


class CSPConnection(connection: Connection, fileWriter: FileOutputStream) extends Actor {


  override def receive: Receive = {

    case Command("login", connection) => {
      val tcpActorReference = context.actorOf(TCPClient.props(connection.serverAddress, self), connection.username)

    }

    case data: ByteString => {
      fileWriter.write(data.toArray)
      val response = data.utf8String
      println(">> " + response)
    }

    case Command("close", connection) => {
      //close
    }

    case message: String =>
      println(message)

    case c: Connected => {
      val login = "5022=LoginUser|5026=1|5079=1|5028=" + connection.username + "|5029=" + connection.password
      val status: String = "5022=ListAvailableTokens|5026=1"

      println("connected to..." + c.remoteAddress)
      sender() ! serialize(login)
      sender() ! serialize(status)
    }

    case _ =>
      println("No matching action...")


  }

  val EOT = Array(0x04.toByte)
  val ETX = Array(0x03.toByte)
  val SPC = Array(0x20.toByte)
  val header = Array.concat(EOT, SPC)
  val CTF_FRAME_OVERHEAD_SIZE = 7

  def serialize(msg: String): ByteString = {
    val payloadLength = Utils.htonl(msg.length)
    val messageToCSP = Array.concat(header, payloadLength, msg.getBytes, ETX)
    ByteString.fromArray(messageToCSP)
  }

  def serializeASCII(msg: String): ByteString = {
    ByteString.fromString(msg, "ASCII")
  }

  //  def deserialize()

}
