package com.zp.file.type

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.zp.file.common.FileManageHelp
import com.zp.file.content.*

class FileTypeManage {

    private object Builder {
        val MANAGER = FileTypeManage()
    }

    companion object {
        fun getInstance() = Builder.MANAGER
    }

    var fileType: FileType = OtherType()

    fun openFile(filePath: String, view: View, context: Context) {
        fileType = getFileType(filePath)
        fileType.openFile(filePath, view, context)
    }

    fun loadingFile(filePath: String, pic: ImageView) {
        fileType = getFileType(filePath)
        fileType.loadingFile(filePath, pic)
    }

    fun infoFile(bean: FileBean, context: Context) {
        fileType = getFileType(bean.filePath)
        fileType.infoFile(bean, context)
    }

    fun getFileType(filePath: String) =
            FileManageHelp.getInstance().getFileTypeListener()?.getFileType(filePath) ?: OtherType()

}