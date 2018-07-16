package com.zp.file.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.view.View
import java.io.File

object FileOpenUtil {

    private const val TXT = "text/plain"
    private const val ZIP = "application/x-zip-compressed"
    // Word
    private const val DOC = "application/msword"
    // excel
    private const val XLS = "application/vnd.ms-excel"
    // ppt
    private const val PPT = "application/vnd.ms-powerpoint"
    // pdf
    private const val PDF = "application/pdf"


    fun openTXT(filePath: String, view: View, context: Context) {
        open(filePath, context, TXT)
    }

    fun openZIP(filePath: String, view: View, context: Context) {
        open(filePath, context, ZIP)
    }

    fun openDOC(filePath: String, view: View, context: Context) {
        open(filePath, context, DOC)
    }

    fun openXLS(filePath: String, view: View, context: Context) {
        open(filePath, context, XLS)
    }

    fun openPPT(filePath: String, view: View, context: Context) {
        open(filePath, context, PPT)
    }

    fun openPDF(filePath: String, view: View, context: Context) {
        open(filePath, context, PDF)
    }

    private fun open(filePath: String, context: Context, type: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
            addCategory("android.intent.category.DEFAULT")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 或者不使用FileProvider ，直接将注释放开，删除file_manage_paths.xml文件也可以打开
                /*val builder = StrictMode.VmPolicy.Builder()
                StrictMode.setVmPolicy(builder.build())
                val contentUri = Uri.fromFile(File(filePath))*/
                val contentUri = FileProvider.getUriForFile(context,
                        "${context.packageName}.fileManager.provider", File(filePath))
                setDataAndType(contentUri, type)
            } else {
                val uri = Uri.fromFile(File(filePath))
                setDataAndType(uri, type)
            }
        })
    }

}