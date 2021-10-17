package com.vaca.car.pop

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.vaca.car.R



object O2RingDialog {

    var dialog: Dialog? = null

    fun showO2RingDialog(
        context: Context?,
        pr: Int,
        o2: Int
    ) {
        if (dialog != null) {
            return
        }

        val resource = R.layout.o2ring_dialog
        val dialogStyle = R.style.showDialog
        val gravity = Gravity.LEFT + Gravity.TOP
        val width = WindowManager.LayoutParams.WRAP_CONTENT
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        val animation = R.style.AnimLeft


        try {
            dialog?.dismiss()
        } catch (e: Exception) {

        }

        val view: View = View.inflate(context, resource, null)


        val o2Tv: TextView = view.findViewById(R.id.o2)
        val prTv: TextView = view.findViewById(R.id.pr)

        o2Tv.text = o2.toString()
        prTv.text = pr.toString()



        dialog = context?.let { Dialog(it, dialogStyle) }
        dialog?.setContentView(view)
        val layoutParams: WindowManager.LayoutParams? = dialog?.getWindow()?.getAttributes()
        if (layoutParams != null) {
            layoutParams.width = width
        }
        if (layoutParams != null) {
            layoutParams.height = height
        }
        if (layoutParams != null) {
            layoutParams.y = 0
            layoutParams.x = 0
        };//距离顶部的距离
        dialog?.apply {
            window?.attributes = layoutParams
            window?.setGravity(gravity)
            window?.setWindowAnimations(animation)
            show()
        }


    }

    fun removeDialog() {
        dialog?.dismiss()
        dialog = null
    }
}