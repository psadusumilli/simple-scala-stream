val EOT = '4'

//val packet = Array.concat("C9E198A0".getBytes + Array(0xff.toByte, 0xff.toByte) + "29832".getBytes)
val packet =  Array.concat(Array(0xff.toByte, 0x04.toByte), "apple".getBytes)
print(EOT)
print(packet)
