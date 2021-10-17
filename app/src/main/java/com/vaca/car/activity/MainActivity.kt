package com.vaca.car.activity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import com.vaca.car.MainApplication
import com.vaca.car.R
import com.vaca.car.ble.BleServer
import com.vaca.car.pop.O2RingDialog
import com.vaca.car.utils.Tts
import kotlinx.coroutines.*
import java.lang.Runnable

class MainActivity : BaseActivity() {
    companion object{
        lateinit var mBluetoothAdapter: BluetoothAdapter
        val bleHandler= Handler()
    }

    val bleTask= object: Runnable {
        override fun run() {
            val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            mBluetoothAdapter = bluetoothManager.adapter
            val scanner=mBluetoothAdapter.bluetoothLeScanner
            if(scanner!=null){
                BleServer.setScan(scanner)
                BleServer.scan.start()
            }else{
                bleHandler.postDelayed(this,3000)
            }


        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Tts.textToSpeech= TextToSpeech(this,object:TextToSpeech.OnInitListener{
            override fun onInit(p0: Int) {
            }
        })

        checkPermission()
    }

    override fun onPermissionGranted() {
        BleServer.dataScope.launch {
            ScanBle()
        }

        GlobalScope.launch {
            while (true){
                delay(20000)
                BleServer.scan.stop()
                delay(4000)
                BleServer.scan.start()
            }
        }

    }

    suspend fun ScanBle(){
        BleServer.setScanCallBack(MainApplication.application)
        delay(1000)
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        //获取BluetoothAdapter
        mBluetoothAdapter = bluetoothManager.adapter

        if (!mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.enable()
        } else {
            bleHandler.postDelayed(bleTask,1000)
        }
    }
}