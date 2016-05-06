import java.io.{File, FileReader}

import akka.stream.scaladsl.{Flow, Source}
import akka.util.ByteString
import akka.stream.io._
import akka.stream.javadsl.FileIO

val cspResponseFile = new File("C:\\Users\\padusumilli\\scalaPlay\\SimpleStreams\\response.dat")

val echo = Flow[ByteString]
  .via(Framing.delimiter(ByteString("\n",256, true))
    .

val source = FileIO.fromFile(cspResponseFile)



