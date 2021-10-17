package com.vaca.car.ble

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object BleServer {
    val dataScope = CoroutineScope(Dispatchers.IO)
    val scan = BleScanManager()
    val dataWorker = BleDataWorker()
    var connectFlag=false
    val connectLiveFlag=MutableLiveData<Boolean>()


    fun setScan(bluetoothLeScanner: BluetoothLeScanner) {
        scan.setScan(bluetoothLeScanner)
    }
    fun setScanCallBack(app: Application) {
        scan.setCallBack(object : BleScanManager.Scan {
            override fun scanReturn(
                name: String,
                bluetoothDevice: BluetoothDevice,
                rssi: Int,
                press: Boolean
            ) {
                if (name.contains("esp32Car")) {
                    if(!connectFlag){
                        connectFlag=true
                        dataWorker.initWorker(app,bluetoothDevice)
                    }
                }
            }
        })
    }
}