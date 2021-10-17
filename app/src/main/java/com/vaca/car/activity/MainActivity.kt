package com.vaca.car.activity

import android.bluetooth.BluetoothManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import com.vaca.car.MainApplication
import com.vaca.car.R
import com.vaca.car.ble.BleServer
import com.vaca.car.pop.O2RingDialog
import com.vaca.car.utils.Tts
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
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
        Log.e("fuck", "fuckfuck")

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