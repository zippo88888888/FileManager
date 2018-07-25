package com.zp.filemanage.custom

import android.content.Context
import android.view.View
import com.zp.file.content.toast
import com.zp.file.listener.JumpByTypeListener

class MyJumpListener : JumpByTypeListener() {

    /**
     * 自定义跳转
     */
    fun jumpCustom(filePath: String, view: View, context: Context) {
        context.toast("这个是我自定义的文件类型，跳转 Custom")
    }
}