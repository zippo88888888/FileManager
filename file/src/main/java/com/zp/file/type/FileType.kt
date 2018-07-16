package com.zp.file.type

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.zp.file.common.FileManageHelp

interface FileType {

    /**
     * 打开文件
     * @param filePath  文件路径
     * @param view      当前视图，配合跳转动画使用
     * @param context   Context
     */
    fun openFile(filePath: String, view: View, context: Context)

    /**
     * 加载文件
     * @param filePath 文件路径
     * @param pic      文件展示的图片
     */
    fun loadingFile(filePath: String, pic: ImageView)

    /**
     * 文件详情
     * @param filePath 文件路径
     */
    fun infoFile(filePath: String, context: Context) {
        FileManageHelp.getInstance().getFileInfoListener()?.fileInfo(filePath, context)
    }
}