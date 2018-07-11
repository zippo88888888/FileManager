package com.zp.file.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zp.file.R
import com.zp.file.common.FileManageHelp
import com.zp.file.content.setStatusBarTransparent
import kotlinx.android.synthetic.main.activity_pic.*

class PicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic)
        setStatusBarTransparent()
        val filePath = intent.getStringExtra("filePath")
        FileManageHelp.getInstance().getImageLoad()?.loadImage(pic_show, filePath)
        pic_show.setOnClickListener { onBackPressed() }
    }
}
