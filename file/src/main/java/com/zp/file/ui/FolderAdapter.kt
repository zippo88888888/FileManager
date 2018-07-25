package com.zp.file.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.zp.file.R
import com.zp.file.common.FileViewHolder
import com.zp.file.content.FileBean
import com.zp.file.type.FileTypeManage

class FolderAdapter(private var context: Context) : RecyclerView.Adapter<FileViewHolder>() {

    /** 数据集合 */
    private var data: ArrayList<FileBean> = ArrayList()

    private var itemClicklistener: ((FileBean, Int) -> Unit)? = null

    fun setItemClickListener(itemClicklistener: (FileBean, Int) -> Unit) {
        this.itemClicklistener = itemClicklistener
    }

    fun setData(data: ArrayList<FileBean>) {
        this.data.clear()
        notifyDataSetChanged()
        if (this.data.addAll(data)) {
            notifyItemRangeChanged(0, data.size)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isFile) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FileViewHolder {
        val view = when (viewType) {
            0 -> LayoutInflater.from(context).inflate(R.layout.item_files, parent, false)
            1 -> LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)
            else -> LayoutInflater.from(context).inflate(R.layout.item_files, parent, false)
        }
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder?, position: Int) {
        val item = getItem(position)
        holder?.apply {
            if (item.isFile) { // 文件
                setTextView(R.id.item_file_name, item.fileName)
                setTextView(R.id.item_file_date, item.date)
                setTextView(R.id.item_file_size, item.size)
                getView<SwipeMenuView>(R.id.item_file_swipeMenuView).apply {
                    isSwipeEnable = false
                }
                getView<ImageView>(R.id.item_file_pic).apply {
                    FileTypeManage.getInstance().loadingFile(item.filePath, this)
                }
                holder.setViewVisibility(R.id.item_file_box_pic, View.GONE)
                holder.setViewVisibility(R.id.item_file_box, View.GONE)
            } else { // 文件夹
                setTextView(R.id.item_file_Jname, item.fileName)
                setOnItemClickListener(View.OnClickListener { itemClicklistener?.invoke(item, position) })
            }
        }
    }

    override fun getItemCount() = data.size
    private fun getItem(position: Int) = data[position]
}