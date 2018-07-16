package com.zp.filemanage

import android.content.Context
import android.view.View
import com.zp.file.content.toast
import com.zp.file.listener.JumpByTypeListener

class MyJumpListener : JumpByTypeListener() {

    override fun jumpAudio(filePath: String, view: View, context: Context) {
        context.toast("跳转 Audio")
    }

    override fun jumpImage(filePath: String, view: View, context: Context) {
        context.toast("跳转 Image")
    }

    override fun jumpVideo(filePath: String, view: View, context: Context) {
        context.toast("跳转 Video")
    }

    override fun jumpTxt(filePath: String, view: View, context: Context) {
        context.toast("跳转 Txt")
    }

    override fun jumpZip(filePath: String, view: View, context: Context) {
        context.toast("跳转 Zip")
    }

    override fun jumpOther(filePath: String, view: View, context: Context) {
        context.toast("跳转 Other")
    }

    override fun jumpDoc(filePath: String, view: View, context: Context) {
        context.toast("跳转 Doc")
    }

    override fun jumpXls(filePath: String, view: View, context: Context) {
        context.toast("跳转 Xls")
    }

    override fun jumpPpt(filePath: String, view: View, context: Context) {
        context.toast("跳转 Ppt")
    }

    override fun jumpPdf(filePath: String, view: View, context: Context) {
        context.toast("跳转 Pdf")
    }
}