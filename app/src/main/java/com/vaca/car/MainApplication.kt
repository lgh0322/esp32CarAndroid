package com.vaca.car

import android.app.Application
import android.util.Log
import com.jdiot.sputils.base.PreferencesConstants
import com.tencent.bugly.crashreport.CrashReport
import com.viatom.littlePu.umeng.PushHelper
import com.viatom.littlePu.utils.JDPreferenceUtil
import com.viatom.littlePu.utils.PathUtil


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