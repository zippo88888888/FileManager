package com.zp.file.type

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.zp.file.R
import com.zp.file.common.FileManageHelp

open class PdfType : FileType {

    override fun openFile(filePath: String, view: View, context: Context) {
        FileManageHelp.getInstance().getJumpListener()?.jumpPdf(filePath, view, context)
    }

    override fun loadingFile(filePath: String, pic: ImageView) {
        pic.setImageResource(R.drawable.file_pdf)
    }
}