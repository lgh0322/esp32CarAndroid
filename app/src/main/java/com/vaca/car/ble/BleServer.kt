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
                if (name.contains("ER2") || name.contains("DuoEK")) {
                    var canGo = false
                    if (press) {
                        ecgWantConnectWay = 0
                        if (!er2ConnectFlag) {
                            Log.i("dada1", "dada999111")
                            if (MyStorage.currentBindEr2Addr.isNotEmpty()) {
                                if (MyStorage.currentBindEr2Addr == bluetoothDevice.address) {
                                    canGo = true
                                }
                            } else {
                                if (MyStorage.currentInEcg) {
                                    if (rssi > -60) {
                                        canGo = true
                                    }
                                }
                            }
                        } else {
                            Log.i("dada1", "dada9991112222")
                        }
                    } else {
                        Log.i("dada1", "dada9991113333")
                        if (MyStorage.currentInEcg) {
                            Log.i("dada1", "dada9991114444")
                            if (MyStorage.currentBindEr2Addr.isNotEmpty()) {
                                Log.i("dada1", "dada9991115555")
                                if (MyStorage.currentBindEr2Addr == bluetoothDevice.address) {
                                    ecgWantConnectWay = 1
                                    canGo = true
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}