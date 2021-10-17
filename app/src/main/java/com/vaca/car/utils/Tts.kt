package com.vaca.car.utils

import android.speech.tts.TextToSpeech
import android.view.View

object Tts {
   var textToSpeech // TTS对象
            : TextToSpeech? = null

    fun init(){

    }

    fun speak(view: String) {
        if (textToSpeech != null && !textToSpeech!!.isSpeaking) {
            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech!!.setPitch(1.0f)
            //设定语速 ，默认1.0正常语速
            textToSpeech!!.setSpeechRate(1.0f)
            //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
            textToSpeech!!.speak(view, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}