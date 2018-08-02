package com.zp.file.type

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.zp.file.R
import com.zp.file.common.FileManageHelp

/**
 * com.zp.file.type
 * Created by ZP on 2018/7/4.
 * description:
 * version: 1.0
 */
open class OtherType : FileType {

    override fun openFile(filePath: String, view: View, context: Context) {
        FileManageHelp.getInstance().getJumpListener()?.jumpOther(filePath, view, context)
    }

    override fun loadingFile(filePath: String, pic: ImageView) {
        pic.setImageResource(R.drawable.file_other)
    }
}