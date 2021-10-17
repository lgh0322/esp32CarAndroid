package com.vaca.car.ble


import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.vaca.car.MainApplication
import com.vaca.car.activity.MainActivity
import com.vaca.car.utils.CRCUtils
import com.vaca.car.utils.add
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import no.nordicsemi.android.ble.callback.DataReceivedCallback
import no.nordicsemi.android.ble.callback.FailCallback
import no.nordicsemi.android.ble.data.Data
import no.nordicsemi.android.ble.observer.ConnectionObserver
import org.greenrobot.eventbus.EventBus
import java.lang.System.currentTimeMillis
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.inv

class BleDataWorker {

    data class Pc100Data(
        val event: Pc100Event,
        val time: Long,
        val sys: Int = 0,
        val dia: Int = 0,
        val pr: Int = 0,
        val o2: Int = 0,
        val o2pr: Int = 0
    )

    enum class Pc100Event {
        GoToResult, O2Data, BpData, TakeOffO2, BpPresent
    }


    private var pool: ByteArray? = null
    private val fileChannel = Channel<Int>(Channel.CONFLATED)
    private val connectChannel = Channel<String>(Channel.CONFLATED)
    var myBleDataManager: BleDataManager? = null
    private val dataScope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()






    var lock = Mutex()

    fun poccessSingleCmd(byteArray: ByteArray) {
        byteArray.apply {
            val size = this.size
            Log.e("getTemp", byteArray2String(this))

        }

    }

    var linkData : ByteArray?=null



    private fun handleDataPool(bytes: ByteArray?): ByteArray? {
        val bytesLeft: ByteArray? = bytes

        if (bytes == null || bytes.size < 4) {
            return bytes
        }
        loop@ for (i in 0 until bytes.size - 3) {
            if (bytes[i] != 0xAA.toByte() || bytes[i + 1] != 0x55.toByte()) {
                continue@loop
            }

            // need content length
            val len = bytes[i+3].toUByte().toInt()
            if (i + 4 + len > bytes.size) {
                continue@loop
            }

            val temp: ByteArray = bytes.copyOfRange(i, i + 4 + len)
            if (temp.last() == CRCUtils.calCRC8(temp)) {

                poccessSingleCmd(temp)
                val tempBytes: ByteArray? =
                    if (i + 4 + len == bytes.size) null else bytes.copyOfRange(
                        i + 4 + len,
                        bytes.size
                    )

                return handleDataPool(tempBytes)
            }
        }

        return bytesLeft
    }


    private val comeData = object : BleDataManager.OnNotifyListener {
        override fun onNotify(device: BluetoothDevice?, data: Data?) {
            val valuex = data?.value
            if (valuex != null) {
                dataScope.launch {
                    lock.withLock {
                        linkData=add(linkData,valuex)
                        linkData=handleDataPool(linkData)
                    }
                }

            }

        }
    }


    fun byteArray2String(byteArray: ByteArray): String {
        var fuc = ""
        for (b in byteArray) {
            val st = String.format("%02X", b)
            fuc += ("$st  ");
        }
        return fuc
    }

    fun sendCmd(bs: ByteArray) {
        myBleDataManager?.sendCmd(bs)
    }

    var job:Job?=null
    var jobStop=false

    private val connectState = object : ConnectionObserver {
        override fun onDeviceConnecting(device: BluetoothDevice) {

        }

        override fun onDeviceConnected(device: BluetoothDevice) {



        }

        override fun onDeviceFailedToConnect(device: BluetoothDevice, reason: Int) {

        }

        override fun onDeviceReady(device: BluetoothDevice) {

        }

        override fun onDeviceDisconnecting(device: BluetoothDevice) {
            myBleDataManager?.refreshDeviceCache()?.enqueue()
        }

        override fun onDeviceDisconnected(device: BluetoothDevice, reason: Int) {

        }

    }




    fun initWorker(context: Context, bluetoothDevice: BluetoothDevice?) {


        bluetoothDevice?.let {
            myBleDataManager?.connect(it)
                ?.useAutoConnect(false)
                ?.retry(8, 100)
                ?.done {



                }?.fail(object : FailCallback {
                    override fun onRequestFailed(device: BluetoothDevice, status: Int) {

                    }

                })
                ?.enqueue()
        }
    }

    suspend fun waitConnect() {
        connectChannel.receive()
    }

    init {
        myBleDataManager = BleDataManager(MainApplication.application)
        myBleDataManager?.setNotifyListener(comeData)
        myBleDataManager?.setConnectionObserver(connectState)
    }



    fun disconnect() {
//        BleServer.pc100ConnectFlag = false
        myBleDataManager?.disconnect()?.enqueue()
    }
}