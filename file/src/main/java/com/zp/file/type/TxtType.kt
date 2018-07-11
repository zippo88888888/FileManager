package com.zp.file.type

import android.content.Context
import android.widget.ImageView
import com.zp.file.R
import android.content.Intent
import android.net.Uri
import android.view.View
import com.zp.file.common.FileManageHelp
import java.io.File

class TxtType : FileType {

    /**
     * 调用系统文本打开
     */
    override fun openFile(filePath: String, view: View, context: Context) {
        FileManageHelp.getInstance().getJumpListener()?.jumpTxt(filePath, view, context)
    }

    override fun loadingFile(filePath: String, pic: ImageView) {
        pic.setImageResource(R.drawable.file_txt)
    }

}