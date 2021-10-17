package com.vaca.car.activity

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import com.vaca.car.R
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
        MainScope().launch {
            delay(1000)
            Tts.speak("蓝牙已连接")
        }
        checkPermission()
    }

    override fun onPermissionGranted() {
        Log.e("fuck", "fuckfuck")

    }
}