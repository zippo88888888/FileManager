package com.zp.filemanage.custom

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.zp.file.common.FileManageHelp
import com.zp.file.type.FileType
import com.zp.filemanage.R

const val CUSTOM = "ttf"

/**
 * 自定义文件类型
 */
class CustomType : FileType {
    /**
     * 打开文件
     * @param filePath  文件路径
     * @param view      当前视图，配合跳转动画使用
     * @param context   Context
     */
    override fun openFile(filePath: String, view: View, context: Context) {
        (FileManageHelp.getInstance().getJumpListener() as MyJumpListener).jumpCustom(filePath, view, context)
    }

    /**
     * 加载文件
     * @param filePath 文件路径
     * @param pic      文件展示的图片
     */
    override fun loadingFile(filePath: String, pic: ImageView) {
        pic.setImageResource(R.mipmap.ic_launcher)
    }

}