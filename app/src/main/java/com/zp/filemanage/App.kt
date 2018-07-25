package com.zp.filemanage

import android.app.Application
import com.zp.file.common.FileManageHelp
import com.zp.file.listener.FileInfoListener
import com.zp.file.listener.FileImageListener
import com.zp.file.listener.FileTypeListener
import com.zp.file.listener.JumpByTypeListener

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FileManageHelp.getInstance()
                .setFileTypeListener(FileTypeListener())
                .setImgeLoad(FileImageListener())
                .setJumpListener(JumpByTypeListener())
                .setFileInfoListener(FileInfoListener())
                .setMaxLength(9, "最大选取数量：9")
                .setCanRightTouch(true)
                .setShowHiddenFile(false)
//                .setFileFilterArray(arrayOf(PNG, JPG, GIF, MP3, AAC, MP4, _3GP, TXT, ZIP))
                .setSortordByWhat(FileManageHelp.BY_DEFAULT)
                .setSortord(FileManageHelp.ASC)
                .isShowLog = true
    }

}