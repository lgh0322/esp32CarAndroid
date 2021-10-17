package com.vaca.car.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.vaca.car.R



class DeviceBatteryView : View {
    var canvas: Canvas? = null
    var batteryValue = 100
    private var data: Int = 13
    private val wavePaint = Paint()

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        wavePaint.apply {
            color = getColor(R.color.battery_black)
            style = Paint.Style.FILL
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        drawWave(canvas)

    }


    private fun drawWave(canvas: Canvas) {
        canvas.drawColor(getColor(R.color.white))

        val xx = BitmapFactory.Options()
        xx.inScaled = false
        xx.inPreferredConfig = Bitmap.Config.ARGB_8888

        val mi = BitmapFactory.decodeResource(resources, R.drawable.devicepower_icon, xx)


        canvas.drawBitmap(mi, Rect(0, 0, 36, 18), Rect(0, 0, width, height), wavePaint)

        if (batteryValue > 100) {
            data = 15
        }
        if (batteryValue < 0) {
            data = 0
        } else {
            data = batteryValue * 24 / 100
        }
        data = data * width / 36
        canvas.drawRect(
            Rect(
                4 * width / 36,
                4 * height / 18,
                4 * width / 36 + data,
                14 * height / 18
            ), wavePaint
        )
    }

    private fun getColor(resource_id: Int): Int {
        return ContextCompat.getColor(context, resource_id)
    }
}