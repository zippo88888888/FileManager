package com.zp.filemanage.custom

import com.zp.file.listener.FileTypeListener
import com.zp.file.type.FileType


/**
 * com.zp.filemanage.custom
 * Created by ZP on 2018/7/25.
 * description:
 * version: 1.0
 */
class MyFileTypeListener : FileTypeListener() {

    override fun getFileType(filePath: String): FileType {
        val typeStr = filePath.run {
            substring(lastIndexOf(".") + 1, length)
        }
        return when (typeStr) {
            CUSTOM -> CustomType()
            else -> super.getFileType(filePath)
        }
    }

}