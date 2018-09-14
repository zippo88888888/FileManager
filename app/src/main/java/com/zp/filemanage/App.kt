package com.zp.filemanage

import android.app.Application
import com.zp.file.common.FileManageHelp
import com.zp.file.listener.FileInfoListener
import com.zp.file.listener.FileImageListener
import com.zp.file.listener.FileZipListener
import com.zp.filemanage.custom.MyFileTypeListener
import com.zp.filemanage.custom.MyJumpListener

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FileManageHelp.getInstance()
                .setFileTypeListener(MyFileTypeListener())
                .setImgeLoad(FileImageListener())
                .setJumpListener(MyJumpListener())
                .setFileZipListener(FileZipListener())
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