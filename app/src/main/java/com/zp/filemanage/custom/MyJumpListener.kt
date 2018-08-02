package com.zp.filemanage.custom

import android.content.Context
import android.view.View
import com.zp.file.content.toast
import com.zp.file.listener.JumpByTypeListener

class MyJumpListener : JumpByTypeListener() {

    override fun jumpOther(filePath: String, view: View, context: Context) {
        if (filePath.lastIndexOf(".") > 0) {
            context.toast("暂不支持【${filePath.run {
                substring(lastIndexOf(".") + 1, length)
            }}】类型的文件预览")
        } else {
            super.jumpOther(filePath, view, context)
        }
    }

    /**
     * 自定义跳转
     */
    fun jumpCustom(filePath: String, view: View, context: Context) {
        context.toast("这个是我自定义的文件类型，跳转 Custom")
    }
}