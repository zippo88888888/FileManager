package com.zp.file.type

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.zp.file.common.FileManageHelp

class ImageType : FileType {

    override fun openFile(filePath: String, view: View, context: Context) {
//        context.jumpActivity(PicActivity::class.java, ArrayMap<String, Any>().apply {
//            put("filePath", filePath)
//        })
        FileManageHelp.getInstance().getJumpListener()?.jumpImage(filePath, view, context)
    }

    override fun loadingFile(filePath: String, pic: ImageView) {
        FileManageHelp.getInstance().getImageLoad()?.loadImage(pic, filePath)
    }

}