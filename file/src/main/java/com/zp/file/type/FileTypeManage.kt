package com.zp.file.type

import android.content.Context
import android.view.View
import android.widget.ImageView
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

    fun infoFile(filePath: String) {
        fileType = getFileType(filePath)
        fileType.infoFile(filePath)
    }

    fun getFileType(filePath: String): FileType {
        val index = filePath.lastIndexOf(".")
        val typeStr = filePath.substring(index + 1, filePath.length)
        return when (typeStr) {
            PNG, JPG, GIF -> ImageType()
            MP3, AAC -> AudioType()
            MP4, _3GP -> VideoType()
            TXT -> TxtType()
            ZIP -> ZipType()
            else -> OtherType()
        }
    }
}