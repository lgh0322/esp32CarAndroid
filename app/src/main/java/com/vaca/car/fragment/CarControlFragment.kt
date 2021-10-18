package com.vaca.car.fragment

import android.graphics.*
import android.hardware.camera2.*
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vaca.car.R
import com.vaca.car.ble.BleCmd
import com.vaca.car.ble.BleServer
import com.vaca.car.databinding.FragmentCarControlBinding
import com.vaca.car.net.Response
import com.vaca.car.net.TcpClient
import com.vaca.car.net.TcpCmd
import com.vaca.car.pop.WifiPop
import com.vaca.car.utils.CRCUtils
import com.vaca.car.utils.add
import com.vaca.car.utils.toUInt
import com.vaca.car.view.JoystickView
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.ByteArrayInputStream
import java.util.*
import kotlin.experimental.inv

class CarControlFragment : Fragment() {
    lateinit var binding: FragmentCarControlBinding
    val dataScope = CoroutineScope(Dispatchers.IO)


    var pool: ByteArray? = null

    var carleft = 1500
    var carright = 1500

    var carRunbase = 1500
    var carTurnbase = 0


    fun calcControl() {
        carleft = carRunbase + carTurnbase
        carright = carRunbase - carTurnbase


        carright = 3000 - carright

    }

    fun controlCar() {
        dataScope.launch {
            calcControl()
            val b = ByteArray(4) {
                0
            }
            b[0] = carleft.and(0xff).toByte()
            b[1] = carleft.shr(8).and(0xff).toByte()
            b[2] = carright.and(0xff).toByte()
            b[3] = carright.shr(8).and(0xff).toByte()
            TcpClient.send(TcpCmd.carRun(b))
        }
    }

    var wifiPop:WifiPop?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentCarControlBinding.inflate(inflater, container, false)




        wifiPop= WifiPop(requireContext())
        MainScope().launch {
            delay(200)
            wifiPop?.showAtLocation(binding.root,Gravity.CENTER,0,0)
        }





        TcpClient.receive = object : TcpClient.Receive {
            override fun tcpReceive(byteArray: ByteArray) {
                pool = add(pool, byteArray)
                pool = poccessLinkData()
            }
        }


        binding.left.setOnJoystickMoveListener(object : JoystickView.OnJoystickMoveListener {
            override fun onValueChanged(angle: Int, power: Int, direction: Int) {
                BleServer.dataWorker.sendCmd(BleCmd.reset())
                val a1 = angle.toDouble() / 360.0 * 2.0 * Math.PI
                val k1 = (Math.sin(a1)) * power.toDouble() / 200.0 * 500.0 + 1500.0
                val k2 = (Math.cos(a1)) * power.toDouble() / 200.0 * 500.0 + 1500.0
                val k22 = k2.toInt()
                Log.e("fuck1", k22.toString())
                carRunbase = k22
                controlCar()
            }
        }, 100)

        binding.right.setOnJoystickMoveListener(object : JoystickView.OnJoystickMoveListener {
            override fun onValueChanged(angle: Int, power: Int, direction: Int) {
                BleServer.dataWorker.sendCmd(BleCmd.setWifi())
                val a1 = angle.toDouble() / 360.0 * 2.0 * Math.PI
                val k1 = (Math.sin(a1)) * power.toDouble()
                val k11 = k1.toInt()
                Log.e("fuck1", k11.toString())
                carTurnbase = k11
                controlCar()
            }

        }, 100)


        binding.ota.setOnClickListener {
            TcpClient.send(TcpCmd.carOTA())
        }

        BleServer.connectLiveFlag.observe(viewLifecycleOwner,{
            binding.bleState.setImageResource(
                if (it) {
                    R.drawable.ble_connect
                } else {
                    R.drawable.ble_off
                }
            )
        })

        BleServer.dataScope.launch {
            delay(10000)

        }

        return binding.root
    }


    fun poccessLinkData(): ByteArray? {
        var bytes = pool
        while (true) {
            if (bytes == null || bytes.size < 11) {
                break
            }
            var con = false

            loop@ for (i in 0 until bytes!!.size - 10) {
                if (bytes!![i] != 0xA5.toByte() || bytes[i + 1] != bytes[i + 2].inv()) {
                    continue@loop
                }

                // need content length
                val len = toUInt(bytes.copyOfRange(i + 6, i + 10))
                if (len < 0) {
                    continue@loop
                }
                if (i + 11 + len > bytes.size) {
                    continue@loop
                }

                val temp: ByteArray = bytes.copyOfRange(i, i + 11 + len)
                if (temp.last() == CRCUtils.calCRC8(temp)) {
                    onResponseReceived(Response(temp))
                    val tempBytes: ByteArray? =
                        if (i + 11 + len == bytes.size) null else bytes.copyOfRange(
                            i + 11 + len,
                            bytes.size
                        )

                    bytes = tempBytes
                    con = true
                    break@loop
                }
            }
            if (!con) {
                return bytes
            } else {
                con = false
            }

        }
        return null
    }


    val loc = Mutex()

    fun onResponseReceived(x: Response) {
        when (x.cmd) {
            TcpCmd.CMD_READ_FILE_DATA -> {
                MainScope().launch {
                    loc.withLock {
                        val bb = x.content.clone()
                        val fg = BitmapFactory.decodeStream(ByteArrayInputStream(bb))
                        if (fg != null) {
                            binding.img.setImageBitmap(fg)
                        }
                    }
                }
            }
            TcpCmd.CMD_READ_FILE_START -> {

            }
        }
    }


}