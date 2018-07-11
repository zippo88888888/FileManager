package com.zp.filemanage

import android.app.Application
import com.zp.file.common.FileManageHelp
import com.zp.file.content.*
import com.zp.file.listener.IFileImageListener
import com.zp.file.listener.IJumpListener

/**
 * com.zp.filemanage
 * Created by ZP on 2018/7/6.
 * description:
 * version: 1.0
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FileManageHelp.getInstance()
                .setImgeLoad(IFileImageListener())
                .setJumpListener(IJumpListener())
                .setMaxLength(9, "最大选取数量：9")
                .setCanRightTouch(true)
                .setShowHiddenFile(false)
                .setFileFilterArray(arrayOf(PNG, JPG, GIF, MP3, AAC, MP4, _3GP, TXT, ZIP))
                .setSortordByWhat(FileManageHelp.BY_DEFAULT)
                .setSortord(FileManageHelp.ASC)
                .isShowLog = true
    }

}