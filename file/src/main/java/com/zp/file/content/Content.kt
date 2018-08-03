package com.zp.file.content

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v4.util.ArrayMap
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.zp.file.common.FileManageDialog
import com.zp.file.common.FileManageHelp
import java.io.Serializable

const val PNG = "png"
const val JPG = "jpg"
const val GIF = "gif"

const val MP3 = "mp3"
const val AAC = "aac"
const val WAV = "wav"

const val MP4 = "mp4"
const val _3GP = "3gp"

const val TXT = "txt"
const val XML = "xml"
const val JSON = "json"

// word
const val DOC = "docx"
// excel
const val XLS = "xlsx"
// ppt
const val PPT = "pptx"

// pdf
const val PDF = "pdf"

const val ZIP = "zip"

const val SORT_DIALOG_TAG = "sortDialogTag"
const val AUDIO_DIALOG_TAG = "audioDialogTag"
const val INFO_DIALOG_TAG = "infoDialogTag"
const val FOLDER_DIALOG_TAG = "folderDialogTag"

const val COPY_TYPE = 0x1001
const val CUT_TYPE = 0x1002
const val ZIP_TYPE = 0x1003

/** SD卡的根目录  */
val SD_ROOT by lazy {
    Environment.getExternalStorageDirectory().path
}

/**
 * 获取状态栏高度
 */
fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

/**
 * 获取屏幕的宽，高
 */
fun Context.getDisplay() = IntArray(2).apply {
    val manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    manager.defaultDisplay.getSize(point)
    this[0] = point.x
    this[1] = point.y
}

/**
 * 从 dp 转成为 px
 */
fun Context.dip2px(dpValue: Float) = dpValue * resources.displayMetrics.density + 0.5f

/**
 * 从 px转成为 dp
 */
fun Context.px2dip(pxValue: Float) = pxValue / resources.displayMetrics.density + 0.5f

/**
 * 根据Tag检查是否存在Fragment实例，如果存在就移除！
 */
fun AppCompatActivity.checkFragmentByTag(tag: String) {
    val fragment = supportFragmentManager.findFragmentByTag(tag)
    if (fragment != null) {
        supportFragmentManager.beginTransaction().remove(fragment).commit()
    }
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

/**
 * 跳转Activity
 */
fun Context.jumpActivity(clazz: Any, map: ArrayMap<String, Any>? = null) {
    if (clazz !is Class<*>) return
    startActivity(Intent(this, clazz).apply {
        if (map != null && map.isNotEmpty()) {
            putExtras(Bundle().apply {
                for ((key, value) in map) {
                    when (value) {
                        is Int -> putInt(key, value)
                        is Double -> putDouble(key, value)
                        is Float -> putFloat(key, value)
                        is Long -> putLong(key, value)
                        is Boolean -> putBoolean(key, value)
                        is String -> putString(key, value)
                        is Serializable -> putSerializable(key, value)
                        is Parcelable -> putParcelable(key, value)
                        else -> throw IllegalArgumentException("map type Error")
                    }
                }
            })
        }
    })
}

/** 设置状态栏透明 */
fun Activity.setStatusBarTransparent() {
    val decorView = window.decorView
    val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    decorView.systemUiVisibility = option
    window.statusBarColor = Color.TRANSPARENT
}

fun Context.getColorById(colorID: Int) = ContextCompat.getColor(this, colorID)
fun Context.getStringById(stringID: Int) = resources.getString(stringID)
/** 为DialogFragment设置需要的宽高 */
fun FileManageDialog.setNeedWH() {
    val width = context.getDisplay()[0] * 0.88f
    dialog.window.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun log(msg: String) {
    if (FileManageHelp.getInstance().isShowLog)
        Log.e("file_manager", msg)
}



