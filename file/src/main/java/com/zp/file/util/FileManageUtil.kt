package com.zp.file.util

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import com.zp.file.common.FileManageHelp
import com.zp.file.content.FileBean
import com.zp.file.content.FileInfoBean
import com.zp.file.content.SD_ROOT
import com.zp.file.content.log
import com.zp.file.listener.FileTask
import java.io.*
import java.nio.channels.FileChannel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


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
                    it.lastModified().toString(), getFileSize(it.length()), it.length()))
        }
        throw RuntimeException("请使用\"getList(filePath: String?, listener: (ArrayList<FileBean>) -> Unit)\"方法")
    }

    fun getList(filePath: String?, listener: (ArrayList<FileBean>) -> Unit, isOnlyFolder: Boolean = false) {
        FileTask(this, listener, isOnlyFolder).execute(if (filePath.isNullOrEmpty()) this.filePath else filePath)
    }

    /**
     * 时间戳格式化
     */
    fun getFormatFileDate(seconds: Long) =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).run { format(Date(seconds)) }

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

    /** int类型转时分秒格式 */
    fun secToTime(time: Int): String {
        val timeStr: String?
        val hour: Int
        var minute: Int
        val second: Int
        if (time <= 0) return "00:00"
        else {
            minute = time / 60
            if (minute < 60) {
                second = time % 60
                timeStr = unitFormat(minute) + ":" + unitFormat(second)
            } else {
                hour = minute / 60
                if (hour > 99) return "99:59:59"
                minute %= 60
                second = time - hour * 3600 - minute * 60
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
            }
        }
        return timeStr
    }

    private fun unitFormat(i: Int) = if (i in 0..9) "0$i" else "$i"

    /**
     * 复制文件
     */
    fun copyFile(sourceFile: String, targetFile: String) : Boolean {
        var success = true
        val oldFile = File(sourceFile)
        val outFile = File(targetFile + "/" + oldFile.name)

        var inputChannel: FileChannel? = null
        var outputChannel: FileChannel? = null
        try {
            inputChannel = FileInputStream(oldFile).channel
            outputChannel = FileOutputStream(outFile).channel
            outputChannel?.transferFrom(inputChannel, 0, inputChannel?.size() ?: 0L)
        } catch (e: Exception) {
            if (FileManageHelp.getInstance().isShowLog) {
                e.printStackTrace()
            }
            success = false
        } finally {
            inputChannel?.close()
            outputChannel?.close()
            return success
        }
    }

    /**
     * 剪切文件
     */
    fun cutFile(sourceFile: String, targetFile: String): Boolean {
        var success = copyFile(sourceFile, targetFile)
        success = try {
            File(sourceFile).delete()
        } catch (e: Exception) {
            if (FileManageHelp.getInstance().isShowLog) {
                e.printStackTrace()
            }
            false
        }
        return success
    }

    /**
     * 解压文件
     */
    fun extractFile(zipFileName: String, outPutDir: String): Boolean {
        var flag = true
        var zipInputStream: ZipInputStream? = null
        val zipEntry: ZipEntry
        var outputStream: FileOutputStream? = null
        var name: String
        try {
            zipInputStream = ZipInputStream(FileInputStream(zipFileName))
            zipEntry = zipInputStream.nextEntry
            while (zipEntry != null) {
                name = zipEntry.name
                if (zipEntry.isDirectory) {
                    name = name.substring(0, name.length - 1)
                    val file = File(outPutDir + File.separator + name)
                    file.mkdirs()
                } else {
                    val file = File(outPutDir + File.separator + name)
                    file.createNewFile()
                    outputStream = FileOutputStream(file)
                    var ch = 0
                    val bytes = ByteArray(1024)
                    while (ch != -1) {
                        ch = zipInputStream.read(bytes)
                        outputStream.write(bytes, 0, ch)
                        outputStream.flush()
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            flag = false
            log("解压失败")
        } finally {
            outputStream?.close()
            try {
               zipInputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return flag
        }
    }

    /**
     * 获取媒体信息
     */
    fun getMultimediaInfo(path: String, isVideo: Boolean = false): FileInfoBean {
        var duration = -1
        var width = "0"
        var height = "0"
        var mediaPlayer: MediaPlayer? = null
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
            if (isVideo) {
                width = mediaPlayer.videoWidth.toString()
                height = mediaPlayer.videoHeight.toString()
            }
            duration = mediaPlayer.duration
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer?.release()
            return FileInfoBean(secToTime(duration / 1000), width, height)
        }
    }

    /**
     * 根据路径获取图片的宽、高信息
     */
    fun getImageWH(imagePath: String) : Array<Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        log("width---${options.outWidth} height---${options.outHeight}")
        return arrayOf(options.outWidth, options.outHeight)
    }

}