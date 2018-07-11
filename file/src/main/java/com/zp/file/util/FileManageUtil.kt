package com.zp.file.util

import com.zp.file.content.FileBean
import com.zp.file.content.SD_ROOT
import com.zp.file.listener.FileTask
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * com.zp.file.util
 * Created by ZP on 2018/7/4.
 * description: 2018-7-6 包括防晒霜，共在京东消费 29023.5RMB
 * version: 1.0
 */
class FileManageUtil {

    private object Builder {
        val MANAGER = FileManageUtil()
    }

    companion object {
        fun getInstance() = Builder.MANAGER
    }

    /** 设置开始位置 */
    var filePath = SD_ROOT

    @Deprecated("废弃")
    fun getList(filePath: String?) : ArrayList<FileBean> {
        this.filePath = if(filePath.isNullOrEmpty()) SD_ROOT else filePath
        val list = ArrayList<FileBean>()
        File(this.filePath).listFiles()?.forEach {
            list.add(FileBean(it.name, it.isFile, it.path, getFormatFileDate(it.lastModified()),
                    it.lastModified().toString(), getFileSize(it.length()), ""))
        }
        throw RuntimeException("请使用\"getList(filePath: String?, listener: (ArrayList<FileBean>) -> Unit)\"方法")
    }

    fun getList(filePath: String?, listener: (ArrayList<FileBean>) -> Unit) {
        FileTask(this, listener).execute(if (filePath.isNullOrEmpty()) this.filePath else filePath)
    }

    /**
     * 时间戳格式化
     */
    fun getFormatFileDate(seconds: Long) = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).run {
        format(Date(seconds))
    }!!

    /**
     * 获取指定文件大小
     */
    fun getFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        val byte_size = java.lang.Double.valueOf(df.format(fileS.toDouble()))
        if (byte_size < 1024) {
            return "$byte_size B"
        }
        val kb_size = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1024))
        if (kb_size < 1024) {
            return "$kb_size KB"
        }
        val mb_size = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1048576))
        if (mb_size < 1024) {
            return "$mb_size MB"
        }
        val gb_size = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1073741824))
        if (gb_size < 1024) {
            return "$gb_size GB"
        }
        return ">1TB"
    }
}