package com.vaca.car.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.Context
import android.os.PowerManager
import android.util.Log



class BleScanManager {
    interface Scan {
        fun scanReturn(
            name: String,
            bluetoothDevice: BluetoothDevice,
            rssi: Int,
            press: Boolean = false
        )
    }

    companion object {
        var scanState = false
    }

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var leScanner: BluetoothLeScanner? = null
    private var scan: Scan? = null


    private var leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(
            callbackType: Int,
            result: ScanResult,
        ) {
            super.onScanResult(callbackType, result)
            val device = result.device
            val name = device.name
            val rssi = result.rssi
            if (name == null) {
                return
            }
            if (name.isEmpty()) {
                return
            }
            Log.i("ble scan", name)
            scan?.scanReturn(name, device, rssi)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {}
        override fun onScanFailed(errorCode: Int) {}
    }

    fun setCallBack(scan: BleScanManager.Scan) {
        this.scan = scan
    }

    val settings: ScanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        .build()


    fun setScan(bluetoothLeScanner: BluetoothLeScanner) {
        leScanner = bluetoothLeScanner
    }

    fun start() {
        var scanState = true

        val builder = ScanFilter.Builder()
        val filter = builder.build()
        try {
            leScanner?.startScan(listOf(filter), settings, leScanCallback)
        } catch (e: Exception) {

        }

    }

    fun stop() {
        var scanState = false
        try {
            leScanner?.stopScan(leScanCallback)
        } catch (e: java.lang.Exception) {

        }

    }
}