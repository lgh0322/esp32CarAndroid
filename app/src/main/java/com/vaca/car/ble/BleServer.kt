package com.vaca.car.ble

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.util.Log

object BleServer {
    val scan = BleScanManager()
    fun setScanCallBack(app: Application) {
        scan.setCallBack(object : BleScanManager.Scan {
            override fun scanReturn(
                name: String,
                bluetoothDevice: BluetoothDevice,
                rssi: Int,
                press: Boolean
            ) {
                if (name.contains("esp32Car")) {



                }
            }
        })
    }
}