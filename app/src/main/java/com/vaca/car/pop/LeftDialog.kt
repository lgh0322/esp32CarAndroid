package com.vaca.car.pop

import android.app.Dialog
import android.content.Context


object LeftDialog {

    var dialog: Dialog? = null

    /** * * @param context 上下文 * @param resource 资源 layout布局 * @param dialogStyle 弹出样式 * @param gravity 方向 * @param width 宽 * @param height 高 * @param animation 动画  */
    fun showLeftDialog(
        context: Context?
    ) {

//        val resource= R.layout.member_dialog
//        val dialogStyle= R.style.showDialog
//        val gravity= Gravity.LEFT+ Gravity.TOP
//        val width=WindowManager.LayoutParams.WRAP_CONTENT
//        val height= WindowManager.LayoutParams.WRAP_CONTENT
//        val animation=R.style.AnimLeft
//        val gg= HomeMemberAdapter.currentMember
//
//        var name:String?=null
//        var nick:String?=null
//        var gender:String?=null
//
//        for(k in MyStorage.myHome){
//            if(k.nick==gg){
//                name=k.name
//                nick=k.nick
//                gender=k.gender
//            }
//        }
//
//
//        try {
//            dialog?.dismiss()
//        }catch (e:Exception){
//
//        }
//
//        val view: View = View.inflate(context, resource, null)
//
//        val head:ImageView=view.findViewById(R.id.head)
//        val genderTv:TextView=view.findViewById(R.id.gender)
//        val nameTv:TextView=view.findViewById(R.id.name)
//        val nickTv:TextView=view.findViewById(R.id.nick)
//
//        name?.let {
//            nameTv.text=it
//        }
//
//        nick?.let {
//            nickTv.text=it
//        }
//
////        if(nick.isNullOrEmpty()){
////            nickTv.text=MyStorage.mainNick
////            head.setImageResource(R.drawable.head_me)
////            if(gender=="1"){
////                genderTv.text="男"
////            }else{
////                genderTv.text="女"
////            }
////        }else{
////            if(gender=="1"){
////                head.setImageResource(R.drawable.head_man)
////                genderTv.text="男"
////            }else{
////                head.setImageResource(R.drawable.head_woman)
////                genderTv.text="女"
////            }
////
////        }
//
//        head.setImageResource(R.drawable.head_me)
//        genderTv.text=""
//
//
//
//
//
//
//
//        dialog = context?.let { Dialog(it, dialogStyle) }
//        dialog?.setContentView(view)
//        val layoutParams: WindowManager.LayoutParams? = dialog?.getWindow()?.getAttributes()
//        if (layoutParams != null) {
//            layoutParams.width = width
//        }
//        if (layoutParams != null) {
//            layoutParams.height = height
//        }
//        if (layoutParams != null) {
//            layoutParams.y = Er2DataConvert.dp2px(context,20)
//            layoutParams.x =Er2DataConvert.dp2px(context,20)
//        };//距离顶部的距离
//      dialog?.apply {
//          window?.attributes = layoutParams
//          window?.setGravity(gravity)
//          window?.setWindowAnimations(animation)
//          show()
//      }
//


    }

    fun removeDialog() {
//        dialog?.dismiss()
    }
}