package com.zp.file.type

import android.content.Context
import android.view.View
import android.widget.ImageView

interface FileType {

    fun openFile(filePath: String, view: View, context: Context)

    fun loadingFile(filePath: String, pic: ImageView)

    fun infoFile(filePath: String) {

    }
}