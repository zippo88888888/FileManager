package com.zp.file.common

import android.content.Context
import android.content.Intent
import android.support.v4.util.ArrayMap
import android.view.View
import com.zp.file.content.jumpActivity
import com.zp.file.listener.*
import com.zp.file.type.FileType
import com.zp.file.type.FileTypeManage
import com.zp.file.ui.FileManageActivity
import org.jetbrains.annotations.NotNull
import java.io.*


class FileManageHelp : FileManage {

    private object Builder {
        val MANAGER = FileManageHelp()
    }

    companion object {

        fun getInstance() = Builder.MANAGER

        /** 默认 */
        const val BY_DEFAULT = 0x1000
        /** 根据名字 */
        const val BY_NAME = 0x1001
        /** 根据最后修改时间 */
        const val BY_DATE = 0x1003
        /** 根据大小 */
        const val BY_SIZE = 0x1004

        /** 升序 */
        const val ASC = 0x2001
        /** 降序 */
        const val DESC = 0x2002
    }

    /** 回调，哪个Activity中调用，就在哪个Activity中使用 */
    var fileResultListener: FileResultListener? = null

    /**
     * 设置文件类型监听
     */
    private var fileTypeListener: FileTypeListener? = IFileTypeListener()
    fun getFileTypeListener() = fileTypeListener
    fun setFileTypeListener(fileTypeListener: FileTypeListener): FileManageHelp {
        this.fileTypeListener = fileTypeListener
        return this
    }

    /**
     * 设置图片的加载方式
     */
    private var imageLoadeListener: FileImageListener? = IFileImageListener()
    fun getImageLoad() = imageLoadeListener
    fun setImgeLoad(imageLoadeListener: FileImageListener) : FileManageHelp {
        this.imageLoadeListener = imageLoadeListener
        return this
    }

    /**
     * 设置跳转方式
     */
    private var jumpListener: JumpByTypeListener? = IJumpListener()
    fun getJumpListener() = jumpListener
    fun setJumpListener(jumpListener: JumpByTypeListener): FileManageHelp {
        this.jumpListener = jumpListener
        return this
    }


    /** 设置最大选取数量，-1表示不限制 */
    private var maxLength = -1
    var maxLengthHintStr = ""
    fun getMaxLength() = maxLength
    fun setMaxLength(maxLength: Int, maxLengthHintStr: String = "最多可选取${maxLength}个文件"): FileManageHelp {
        if (maxLength <= 0) throw IllegalArgumentException("maxLength 必须大于 0")
        this.maxLength = maxLength
        this.maxLengthHintStr = maxLengthHintStr
        return this
    }

    /** 设置是否可以滑动 */
    private var canRightTouch = false
    fun getCanRightTouch() = canRightTouch
    fun setCanRightTouch(canRightTouch: Boolean): FileManageHelp {
        this.canRightTouch = canRightTouch
        return this
    }

    /**
     * 是否显示隐藏文件
     */
    private var isShowHiddenFile = false
    fun getShowHiddenFile() = isShowHiddenFile
    fun setShowHiddenFile(@NotNull isShowHiddenFile: Boolean): FileManageHelp {
        this.isShowHiddenFile = isShowHiddenFile
        return this
    }

    /**
     * 设置过滤规则
     */
    private var fileFilterArray: Array<String>? = null
    fun getFileFilterArray() = fileFilterArray
    fun setFileFilterArray(fileFilterArray: Array<String>): FileManageHelp {
        this.fileFilterArray = fileFilterArray
        return this
    }

    /**
     * 设置排序的依据
     */
    private var sortordByWhat = BY_DEFAULT
    fun getSortordByWhat() = sortordByWhat
    fun setSortordByWhat(@NotNull sortordByWhat: Int): FileManageHelp {
        if (sortordByWhat != BY_DEFAULT && sortordByWhat != BY_NAME && sortordByWhat != BY_DATE && sortordByWhat != BY_SIZE)
            throw IllegalArgumentException("sortordByWhat error")
        this.sortordByWhat = sortordByWhat
        return this
    }

    /**
     * 设置排序的方式
     */
    private var sortord = ASC
    fun getSortord() = sortord
    fun setSortord(@NotNull sortord: Int): FileManageHelp {
        if (sortord != ASC && sortord != DESC)
            throw IllegalArgumentException("sortord error")
        this.sortord = sortord
        return this
    }

    /** 是否显示log文件 */
    var isShowLog = true

    /**
     * 跳转至文件管理页面
     * @param path 指定的文件路径
     */
    fun start(context: Context, path: String? = null) {
        context.jumpActivity(FileManageActivity::class.java,
                if (path == null) null else ArrayMap<String,Any>().apply { put("filePath", path) })
    }

    /**
     * 打开文件
     */
    override fun openFile(fileType: FileType?, filePath: String, view: View, context: Context) {
        FileTypeManage.getInstance().openFile(filePath, view, context)
    }

    /**
     * 删除文件
     */
    override fun deleteFile(context: Context, filePath: String, deleteListener: (Boolean) -> Unit) {
        CommonDialog(context).showDialog2({
            deleteListener.invoke(File(filePath).delete())
        }, {}, "你确定要删除吗？", "删除", "取消")

    }

    /**
     * 复制文件
     */
    override fun copyFile(filePath: String) {
    }

    /**
     * 剪切文件
     */
    override fun cutFile(filePath: String) {
    }

    /**
     * 粘贴文件
     */
    override fun pasteFile(filePath: String) {
    }

    /**
     * 查看文件详情
     */
    override fun infoFile(fileType: FileType?, filePath: String) {
        FileTypeManage.getInstance().infoFile(filePath)
    }

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
}