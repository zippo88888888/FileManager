package com.zp.file.listener

import android.os.AsyncTask
import com.zp.file.common.FileManageHelp
import com.zp.file.content.FileBean
import com.zp.file.util.FileManageUtil
import java.io.File
import java.lang.ref.WeakReference
import java.util.ArrayList

class FileTask(util: FileManageUtil, private var listener: (ArrayList<FileBean>) -> Unit, private var isOnlyFolder: Boolean)
    : AsyncTask<String, Unit, ArrayList<FileBean>>() {

    private val week: WeakReference<FileManageUtil> by lazy { WeakReference(util) }

    override fun doInBackground(vararg params: String?): ArrayList<FileBean>? {
        return try {
            getFileList(params[0])
        } catch (e: Exception) {
            e.printStackTrace()
            ArrayList()
        }
    }

    override fun onPostExecute(result: ArrayList<FileBean>) {
        listener.invoke(result)
    }

    private fun getFileList(path: String?): ArrayList<FileBean> {
        val isShowHiddenFile = FileManageHelp.getInstance().getShowHiddenFile()

        val list = ArrayList<FileBean>()
        val file = File(path)
        val listFiles = file.listFiles(LowFileFilter(FileManageHelp.getInstance().getFileFilterArray(), isOnlyFolder))
        listFiles?.forEach {
            if (isShowHiddenFile) { // 是否显示隐藏文件
                val bean = FileBean(it.name, it.isFile, it.path,
                        week.get()?.getFormatFileDate(it.lastModified()) ?: "未知时间",
                        it.lastModified().toString(),
                        week.get()?.getFileSize(it.length()) ?: "0B",
                        "")
                list.add(bean)
            } else {
                if (!it.isHidden) {
                    val bean = FileBean(it.name, it.isFile, it.path,
                            week.get()?.getFormatFileDate(it.lastModified()) ?: "未知时间",
                            it.lastModified().toString(),
                            week.get()?.getFileSize(it.length()) ?: "0B",
                            "")
                    list.add(bean)
                }
            }
        }

        // 排序相关

        val sortordByWhat = FileManageHelp.getInstance().getSortordByWhat()
        val sortord = FileManageHelp.getInstance().getSortord()

        when (sortordByWhat) {
            FileManageHelp.BY_NAME -> list.sortBy { it.fileName }
            FileManageHelp.BY_DATE -> list.sortBy { it.originalDate }
            FileManageHelp.BY_SIZE -> list.sortBy { it.size }
        }
        when (sortord) {
            FileManageHelp.ASC -> {
            }
            FileManageHelp.DESC -> {
            }
        }
        return list
    }
}