package com.zp.file.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zp.file.R
import com.zp.file.common.FileViewHolder
import com.zp.file.content.FileBean
import com.zp.file.content.FilePathBean
import com.zp.file.content.SD_ROOT
import com.zp.file.util.FileManageUtil
import java.io.File


class FilePathAdapter (private var context: Context) : RecyclerView.Adapter<FileViewHolder>() {

    private var list = ArrayList<FilePathBean>()

    init {
        if (FileManageUtil.getInstance().filePath == SD_ROOT)
            list.add(FilePathBean("根目录", "root"))
        else list.add(FilePathBean("指定目录${File(FileManageUtil.getInstance().filePath).name}", "assign"))
    }

    fun addData(fileBean: FileBean, listener: (Boolean) -> Unit) {
        val bean = FilePathBean(fileBean.fileName, fileBean.filePath)
        if (list.add(bean)) {
            notifyItemInserted(list.size)
            listener.invoke(true)
        } else listener.invoke(false)
    }

    fun back() {
        list.removeAt(list.size - 1)
        notifyItemRemoved(list.size)
    }

    override fun onBindViewHolder(holder: FileViewHolder?, position: Int) {
        holder?.apply {
            setTextView(R.id.item_file_path_title, getItem(position).fileName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_file_path, parent, false)
        return FileViewHolder(view)
    }

    override fun getItemCount() = list.size

    private fun getItem(position: Int) = list[position]
}