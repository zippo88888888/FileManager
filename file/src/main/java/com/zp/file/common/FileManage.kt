package com.zp.file.common

import android.content.Context
import android.view.View
import com.zp.file.type.FileType

interface FileManage {

    /**
     * 打开文件
     */
    fun openFile(fileType: FileType?, filePath: String, view: View, context: Context)

    /**
     * 删除文件
     */
    fun deleteFile(context: Context, filePath: String, deleteListener: (Boolean) -> Unit)

    /**
     * 复制文件
     */
    fun copyFile(filePath: String)

    /**
     * 剪切文件
     */
    fun cutFile(filePath: String)

    /**
     * 粘贴文件
     */
    fun pasteFile(filePath: String)

    /**
     * 查看文件详情
     */
    fun infoFile(fileType: FileType?, filePath: String)

}