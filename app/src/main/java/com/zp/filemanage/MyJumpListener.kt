package com.zp.filemanage

import android.content.Context
import android.view.View
import com.zp.file.content.toast
import com.zp.file.listener.JumpByTypeListener

/**
 * com.zp.filemanage
 * Created by ZP on 2018/7/9.
 * description:
 * version: 1.0
 */
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
}