package com.vaca.car.pop

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout

import com.vaca.car.R
import com.vaca.car.utils.KeyboardUtil


@SuppressLint("ClickableViewAccessibility")
class WifiPop(mContext:Context) : PopupWindow() {
    init {
        val view = LayoutInflater.from(mContext).inflate(R.layout.pop_wifi, null)
        isOutsideTouchable = true
        contentView = view
        height = RelativeLayout.LayoutParams.MATCH_PARENT
        width = RelativeLayout.LayoutParams.MATCH_PARENT
        isFocusable = true
        val dw = ColorDrawable(-0x80000000)
        setBackgroundDrawable(dw)
        this.animationStyle = R.style.take_photo_anim


        val ga2:EditText=view.findViewById(R.id.ga2)
        val ga3:EditText=view.findViewById(R.id.ga3)

        ga2.inputType=InputType.TYPE_NULL
        ga3.inputType=InputType.TYPE_NULL


        val viewGroup:ConstraintLayout=view.findViewById(R.id.view_group)


        ga2.setOnClickListener {
            KeyboardUtil.shared(mContext, ga2,viewGroup).showKeyboard()
        }



        val close: ImageView = view.findViewById(R.id.close)
        close.setOnClickListener {
            dismiss()
        }

    }


}