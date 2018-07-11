package com.zp.file.listener

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zp.file.R
import com.zp.file.content.AUDIO_DIALOG_TAG
import com.zp.file.content.checkFragmentByTag
import com.zp.file.content.log
import com.zp.file.type.*
import com.zp.file.ui.AudioPlayDialog
import com.zp.file.ui.PicActivity
import java.io.File

/**
 * 图片加载
 */
interface FileImageListener {
    fun loadImage(imageView: ImageView, path: String)
}

class IFileImageListener : FileImageListener {
    override fun loadImage(imageView: ImageView, path: String) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(imageView.context)
                .load(File(path))
                .placeholder(R.drawable.file_other)
                .error(R.drawable.file_other)
                .dontAnimate()
                .into(imageView)
    }
}

/**
 * 跳转
 */
private interface BaseJumpByTypeListener {
    fun jump(filePath: String, view: View, context: Context)
}

abstract class JumpByTypeListener : BaseJumpByTypeListener {
    final override fun jump(filePath: String, view: View, context: Context) {
        when (FileTypeManage.getInstance().getFileType(filePath)) {
            is AudioType -> jumpAudio(filePath, view, context)
            is ImageType -> jumpImage(filePath, view, context)
            is VideoType -> jumpVideo(filePath, view, context)
            is TxtType -> jumpTxt(filePath, view, context)
            is ZipType -> jumpZip(filePath, view, context)
            is OtherType -> jumpOther(filePath, view, context)
        }
    }
    abstract fun jumpAudio(filePath: String, view: View, context: Context)
    abstract fun jumpImage(filePath: String, view: View, context: Context)
    abstract fun jumpVideo(filePath: String, view: View, context: Context)
    abstract fun jumpTxt(filePath: String, view: View, context: Context)
    abstract fun jumpZip(filePath: String, view: View, context: Context)
    abstract fun jumpOther(filePath: String, view: View, context: Context)
}

class IJumpListener : JumpByTypeListener() {

    override fun jumpAudio(filePath: String, view: View, context: Context) {
        val activity = context as AppCompatActivity
        activity.checkFragmentByTag(AUDIO_DIALOG_TAG)
        AudioPlayDialog.getInstance(filePath).apply {
            show(activity.supportFragmentManager, AUDIO_DIALOG_TAG)
        }
    }

    override fun jumpImage(filePath: String, view: View, context: Context) {
        context.startActivity(Intent(context, PicActivity::class.java).apply {
            putExtra("filePath", filePath)
        }, ActivityOptions.makeSceneTransitionAnimation(context as Activity, view, "jump_Pic").toBundle())
    }

    override fun jumpVideo(filePath: String, view: View, context: Context) {
        log("jumpVideo")
    }

    override fun jumpTxt(filePath: String, view: View, context: Context) {
        log("jumpTxt")
        /*context.startActivity(Intent(Intent.ACTION_VIEW).apply {
            addCategory("android.intent.category.DEFAULT")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            setDataAndType(Uri.fromFile(File(filePath)), "text/plain")
        })*/
    }

    override fun jumpZip(filePath: String, view: View, context: Context) {
        log("jumpZip")
    }

    override fun jumpOther(filePath: String, view: View, context: Context) {
        log("jumpOther")
    }
}


