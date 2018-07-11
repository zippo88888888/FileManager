package com.zp.filemanage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zp.file.common.FileManageHelp
import com.zp.file.content.FileBean
import com.zp.file.listener.FileResultListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FileResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FileManageHelp.getInstance().fileResultListener = this
        /*FileManageHelp.getInstance().fileResultListener = object : FileResultListener {
            override fun resultSuccess(list: ArrayList<FileBean>?) {

            }
        }*/
        main_jump.setOnClickListener {
//            FileManageHelp.getInstance().jumpToFileUI(this, "$SD_ROOT/Pictures")
            FileManageHelp.getInstance().start(this)
        }

    }

    override fun resultSuccess(list: ArrayList<FileBean>?) {
        main_msg.text = StringBuilder().run {
            list?.forEach { append("$it\n\n") }
            toString()
        }
    }
}
