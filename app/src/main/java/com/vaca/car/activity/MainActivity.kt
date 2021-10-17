package com.vaca.car.activity

import android.os.Bundle
import android.util.Log
import com.vaca.car.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }

    override fun onPermissionGranted() {
        Log.e("fuck", "fuckfuck")
    }
}