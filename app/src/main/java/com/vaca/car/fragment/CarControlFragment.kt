package com.vaca.car.fragment

import android.graphics.*
import android.hardware.camera2.*
import android.os.Bundle
import android.util.Log
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentCarControlBinding.inflate(inflater, container, false)





        BleServer.connectLiveFlag.observe(viewLifecycleOwner,{
            binding.bleState.setImageResource(
                if (it) {
                    R.drawable.ble_connect
                } else {
                    R.drawable.ble_off
                }
            )
        })



        return binding.root
    }




}