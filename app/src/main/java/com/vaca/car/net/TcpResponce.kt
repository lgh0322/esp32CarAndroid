package com.vaca.car.net


import com.vaca.car.utils.toUInt
import com.vaca.car.utils.unsigned

class Response(bytes: ByteArray) {
    var cmd: Int = bytes[1].unsigned()
    var id: Int = bytes[3].unsigned()
    var pkgNo: Int = toUInt(bytes.copyOfRange(4, 6))
    var len: Int = toUInt(bytes.copyOfRange(6, 10))
    var content: ByteArray = bytes.copyOfRange(10, 10 + len)
}