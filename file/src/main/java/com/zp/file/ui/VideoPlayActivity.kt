package com.zp.file.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zp.file.R
import com.zp.file.content.setStatusBarTransparent
import kotlinx.android.synthetic.main.activity_video_play.*

class VideoPlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        setStatusBarTransparent()
        val videoPath = intent.getStringExtra("videoFilePath")
        videoPlayer_button.setOnClickListener { v ->
            video_player.videoPath = videoPath
            video_player.play()
            v.visibility = View.GONE
        }
    }
}
