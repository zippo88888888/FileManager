package com.zp.file.listener

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zp.file.R
import com.zp.file.type.*
import java.io.File
import android.support.v7.app.AlertDialog
import com.zp.file.content.*
import com.zp.file.ui.*
import com.zp.file.util.FileOpenUtil


/**
 * 文件类型
 */
open class FileTypeListener {

    open fun getFileType(filePath: String): FileType {
        val typeStr = filePath.run {
            substring(lastIndexOf(".") + 1, length)
        }
        return when (typeStr.toLowerCase()) {
            PNG, JPG, GIF -> ImageType()
            MP3, AAC -> AudioType()
            MP4, _3GP -> VideoType()
            TXT, XML -> TxtType()
            ZIP -> ZipType()
            DOC -> DocType()
            XLS -> XlsType()
            PPT -> PptType()
            PDF -> PdfType()
            else -> OtherType()
        }
    }
}


/**
 * 图片加载
 */
open class FileImageListener {
    open fun loadImage(imageView: ImageView, path: String) {
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
open class JumpByTypeListener {

    open fun jumpAudio(filePath: String, view: View, context: Context) {
        val activity = context as AppCompatActivity
        activity.checkFragmentByTag(AUDIO_DIALOG_TAG)
        AudioPlayDialog.getInstance(filePath).apply {
            show(activity.supportFragmentManager, AUDIO_DIALOG_TAG)
        }
    }

    open fun jumpImage(filePath: String, view: View, context: Context) {
        context.startActivity(Intent(context, PicActivity::class.java).apply {
            putExtra("picFilePath", filePath)
        }, ActivityOptions.makeSceneTransitionAnimation(context as Activity, view,
                context.getStringById(R.string.sharedElement_pic)).toBundle())
    }

    open fun jumpVideo(filePath: String, view: View, context: Context) {
        context.startActivity(Intent(context, VideoPlayActivity::class.java).apply {
            putExtra("videoFilePath", filePath)
        }, ActivityOptions.makeSceneTransitionAnimation(context as Activity, view,
                context.getStringById(R.string.sharedElement_video)).toBundle())
    }

    open fun jumpTxt(filePath: String, view: View, context: Context) {
        log("jumpTxt")
        FileOpenUtil.openTXT(filePath, view, context)
    }

    open fun jumpZip(filePath: String, view: View, context: Context) {
        AlertDialog.Builder(context).apply {
            setTitle("请选择")
            setItems(arrayOf("打开", "解压"), { dialog, which ->
                dialog.dismiss()
                if (which == 0) {
                    FileOpenUtil.openZIP(filePath, view, context)
                } else {
                    val activity = context as FileManageActivity
                    activity.checkFragmentByTag(FOLDER_DIALOG_TAG)
                    // 解压文件
                    FolderDialog.newInstance(filePath).apply {
                        telActivityListener = activity
                        show(activity.supportFragmentManager, FOLDER_DIALOG_TAG)
                    }
                }
            })
            setPositiveButton("取消", { dialog, _ -> dialog.dismiss() })
            show()
        }
    }

    open fun jumpDoc(filePath: String, view: View, context: Context) {
        FileOpenUtil.openDOC(filePath, view, context)
    }

    open fun jumpXls(filePath: String, view: View, context: Context) {
        FileOpenUtil.openXLS(filePath, view, context)
    }

    open fun jumpPpt(filePath: String, view: View, context: Context) {
        FileOpenUtil.openPPT(filePath, view, context)
    }

    open fun jumpPdf(filePath: String, view: View, context: Context) {
        FileOpenUtil.openPDF(filePath, view, context)
    }

    open fun jumpOther(filePath: String, view: View, context: Context) {
        log("jumpOther")
        context.toast("暂不支持预览该文件")
    }
}


/**
 * 文件详情
 */
open class FileInfoListener {
    open fun fileInfo(bean: FileBean, context: Context) {
        (context as AppCompatActivity).apply {
            checkFragmentByTag(INFO_DIALOG_TAG)
            InfoDialog.newInstance(bean).apply {
                show(supportFragmentManager, INFO_DIALOG_TAG)
            }
        }
    }
}


