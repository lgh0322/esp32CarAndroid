package com.vaca.car.activity

import android.os.Bundle
import android.util.Log
import com.vaca.car.R
import com.vaca.car.pop.O2RingDialog
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }

    override fun onPermissionGranted() {
        Log.e("fuck", "fuckfuck")
        O2RingDialog.showO2RingDialog(this, 56, 48)
        MainScope().launch {
            delay(9000)
            O2RingDialog.removeDialog()
        }
    }
}