package com.zp.file.ui

import android.app.Dialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.zp.file.R
import com.zp.file.common.FileManageDialog
import com.zp.file.content.getDisplay
import com.zp.file.content.setNeedWH
import com.zp.file.util.FileManageUtil
import kotlinx.android.synthetic.main.dialog_audio_play_layout.*
import java.lang.ref.WeakReference

class AudioPlayDialog : FileManageDialog(), SeekBar.OnSeekBarChangeListener, Runnable {

    companion object {
        fun getInstance(filePath: String) = AudioPlayDialog().apply {
            arguments = Bundle().apply { putString("filePath", filePath) }
        }
    }

    private val UNIT = -1
    private val PLAY = 0
    private val PAUSE = 1

    private var playerState = UNIT

    // 静态类部类防泄漏
    private var audioHandler: AudioHandler? = null
    private var mediaPlayer: MediaPlayer? = null

    private var beginTime: Long = 0
    private var falgTime: Long = 0
    private var pauseTime: Long = 0

    override fun getContentView() = R.layout.dialog_audio_play_layout

    override fun createDialog(savedInstanceState: Bundle?) = Dialog(context, R.style.Common_Dialog).apply {
        window.setGravity(Gravity.CENTER)
    }

    override fun init(savedInstanceState: Bundle?) {
        audioHandler = AudioHandler(this)
        initPlayer()
        dialog_audio_play.setOnClickListener { // 播放
            if (playerState == PAUSE) {
                startPlay()

                falgTime = SystemClock.elapsedRealtime()
                beginTime = falgTime - dialog_audio_bar.progress
                dialog_audio_nowTime.base = beginTime
                dialog_audio_nowTime.start()
            } else initPlayer()
        }
        dialog_audio_pause.setOnClickListener { // 暂停
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                playerState = PAUSE

                dialog_audio_nowTime.stop()
                pauseTime = SystemClock.elapsedRealtime()

                dialog_audio_play.visibility = View.VISIBLE
                dialog_audio_pause.visibility = View.GONE
                dialog_audio_bar.isEnabled = false
            }
        }
        dialog_audio_bar.setOnSeekBarChangeListener(this)
        dialog_audio_name.text = arguments.getString("filePath").let {
            it.substring(it.lastIndexOf("/") + 1, it.length)
        }
    }

    override fun onStart() {
        super.onStart()
        setNeedWH()
    }

    private fun initPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(arguments.getString("filePath"))
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener { play ->
            dialog_audio_bar.max = play.duration
            audioHandler?.post(this)
            dialog_audio_countTime.text = FileManageUtil.getInstance().secToTime(play.duration / 1000)

            // 设置运动时间
            falgTime = SystemClock.elapsedRealtime()
            pauseTime = 0
            dialog_audio_nowTime.base = falgTime
            dialog_audio_nowTime.start()

            startPlay()
        }
        mediaPlayer?.setOnCompletionListener {
            stopPlay()
            dialog_audio_bar.progress = 0

            dialog_audio_nowTime.base = SystemClock.elapsedRealtime()
            dialog_audio_nowTime.start()
            dialog_audio_nowTime.stop()
        }
    }

    // 开始播放
    private fun startPlay() {
        mediaPlayer?.start()
        playerState = PLAY
        dialog_audio_play.visibility = View.GONE
        dialog_audio_pause.visibility = View.VISIBLE
        dialog_audio_bar.isEnabled = true
    }

    // 停止播放
    private fun stopPlay() {
        dialog_audio_play.visibility = View.VISIBLE
        dialog_audio_pause.visibility = View.GONE
        mediaPlayer?.release()
        mediaPlayer = null
        playerState = UNIT
        dialog_audio_bar.isEnabled = false
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (fromUser && mediaPlayer != null) {
            mediaPlayer?.seekTo(progress)
            falgTime = SystemClock.elapsedRealtime()
            beginTime = falgTime - seekBar.progress
            dialog_audio_nowTime.base = beginTime
            dialog_audio_nowTime.start()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

    // 提前释放资源
    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        // 释放资源
        if (mediaPlayer?.isPlaying == true) mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        playerState = UNIT
        audioHandler?.removeCallbacks(this)
        audioHandler?.removeCallbacksAndMessages(null)
        audioHandler = null
    }

    override fun run() {
        // 获得歌曲现在播放位置并设置成播放进度条的值
        if (mediaPlayer != null) {
            audioHandler?.sendEmptyMessage(0)
            // 每次延迟100毫秒再启动线程
            audioHandler?.postDelayed(this, 100)
        }
    }

    class AudioHandler(dialog: AudioPlayDialog) : Handler() {
        private val week: WeakReference<AudioPlayDialog> by lazy {
            WeakReference<AudioPlayDialog>(dialog)
        }

        override fun handleMessage(msg: Message?) {
            week.get()?.dialog_audio_bar?.progress = week.get()?.mediaPlayer?.currentPosition ?: 0
        }
    }

}