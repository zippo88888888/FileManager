package com.zp.filemanage

import android.app.Application
import com.zp.file.common.FileManageHelp
import com.zp.file.listener.IFileImageListener
import com.zp.file.listener.IFileInfoListener
import com.zp.file.listener.IFileTypeListener
import com.zp.file.listener.JumpByTypeListener

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FileManageHelp.getInstance()
                .setFileTypeListener(IFileTypeListener())
                .setImgeLoad(IFileImageListener())
                .setJumpListener(JumpByTypeListener())
                .setFileInfoListener(IFileInfoListener())
                .setMaxLength(9, "最大选取数量：9")
                .setCanRightTouch(true)
                .setShowHiddenFile(false)
//                .setFileFilterArray(arrayOf(PNG, JPG, GIF, MP3, AAC, MP4, _3GP, TXT, ZIP))
                .setSortordByWhat(FileManageHelp.BY_DEFAULT)
                .setSortord(FileManageHelp.ASC)
                .isShowLog = true
    }

}