package com.zp.filemanage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zp.file.common.FileManageHelp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FileManageHelp.getInstance().fileResultListener = { list ->
            main_msg.text = StringBuilder().run {
                list?.forEach { append("$it\n\n") }
                toString()
            }
        }

        main_jump.setOnClickListener {
//            FileManageHelp.getInstance().jumpToFileUI(this, "$SD_ROOT/Pictures")
            FileManageHelp.getInstance().start(this)
        }

    }
}

/*
        java调用

        FileManageHelp.Companion.getInstance().start(this);

        FileManageHelp.Companion.getInstance().setFileResultListener(new Function1<ArrayList<FileBean>, Unit>() {
            @Override
            public Unit invoke(ArrayList<FileBean> list) {
                return null;
            }
        });

*/
