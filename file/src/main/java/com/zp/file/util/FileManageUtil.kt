package com.zp.file.util

import android.media.MediaPlayer
import com.zp.file.content.FileBean
import com.zp.file.content.FileInfoBean
import com.zp.file.content.SD_ROOT
import com.zp.file.content.log
import com.zp.file.listener.FileTask
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.io.File.separator
import java.nio.file.Files.isDirectory
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
                    it.lastModified().toString(), getFileSize(it.length()), ""))
        }
        throw RuntimeException("请使用\"getList(filePath: String?, listener: (ArrayList<FileBean>) -> Unit)\"方法")
    }

    fun getList(filePath: String?, listener: (ArrayList<FileBean>) -> Unit, isOnlyFolder: Boolean = false) {
        FileTask(this, listener, isOnlyFolder).execute(if (filePath.isNullOrEmpty()) this.filePath else filePath)
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

    private fun unitFormat(i: Int) = if (i in 0..9) "0" + Integer.toString(i) else "" + i

    /**
     * 复制文件
     * @param sourceFile    原文件
     * @param targetFile    复制的文件
     */
    fun copyFile(sourceFile: File, targetFile: File) : Boolean {
        var success = false
        // 新建文件输入流并对它进行缓冲
        val input = FileInputStream(sourceFile)
        val inBuff = BufferedInputStream(input)
        // 新建文件输出流并对它进行缓冲
        val output = FileOutputStream(targetFile)
        val outBuff = BufferedOutputStream(output)
        try {
            // 缓冲数组
            val b = ByteArray(1024 * 5)
            while (true) {
                val len = inBuff.read(b)
                if (len == -1) {
                    break
                } else {
                    outBuff.write(b, 0, len)
                }
            }
            // 刷新此缓冲的输出流
            outBuff.flush()
            success = true
        } catch (e: Exception) {
            e.printStackTrace()
            success = false
        } finally {
            //关闭流
            inBuff.close()
            outBuff.close()
            output.close()
            input.close()
            return success
        }
    }

    /**
     * 解压文件
     */
    fun extractFile(zipFileName: String, outPutDir: String): Int {
        var index = 0
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
            index = -1
            log("解压失败")
        } finally {
            outputStream?.close()
            try {
                if (zipInputStream != null) zipInputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return index
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

}