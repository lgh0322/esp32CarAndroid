package com.vaca.car

import android.app.Application
import android.util.Log


class MainApplication : Application() {

    companion object {
        lateinit var application: Application
    }


    override fun onCreate() {
        super.onCreate()




//
//        PathUtil.initVar(this)
//


        application = this
//        CrashReport.initCrashReport(this, "f10cb23cee", false);
    }


}