package com.vaca.car.utils

import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.SPUtils


object MyStorage {
    const val BindO2RingAddr = "bindo2ringaddr"
    const val BindO2RingName = "bindo2ringname"
    var currentBindO2RingAddr = ""
    var currentBindO2RingName = ""


    init {
        currentBindO2RingAddr = getBindO2RingAddr()
        currentBindO2RingName = getBindO2RingName()
    }







    fun getBindO2RingAddr(): String {
        return SPUtils.getInstance().getString(BindO2RingAddr, "")
    }

    fun setBindO2RingAddr(s: String) {
        currentBindO2RingAddr = s
        SPUtils.getInstance().put(BindO2RingAddr, s)
    }

    fun getBindO2RingName(): String {
        return SPUtils.getInstance().getString(BindO2RingName, "")
    }

    fun setBindO2RingName(s: String) {
        currentBindO2RingName = s
        SPUtils.getInstance().put(BindO2RingName, s)
    }


}