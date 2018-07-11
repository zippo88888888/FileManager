package com.zp.file.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.zp.file.R
import com.zp.file.common.FileManageHelp
import com.zp.file.common.FileViewHolder
import com.zp.file.content.FileBean
import com.zp.file.content.toast
import com.zp.file.type.FileTypeManage
import org.jetbrains.annotations.NotNull
import java.io.File

class FileManageAdapter(private var context: Context) : RecyclerView.Adapter<FileViewHolder>() {

    /** 数据集合 */
    private var data: ArrayList<FileBean> = ArrayList()
    /** CheckBox的状态集合 */
    private var boxStateList = ArrayList<Boolean>()
    /** 已选中的数据集合 */
    private var selectedData = ArrayList<FileBean>()

    private var itemClicklistener: ((View, FileBean, Int) -> Unit)? = null
    private var itemLongClickListener: ((FileBean, Int) -> Boolean)? = null
    private var changeListener: ((Boolean, Int) -> Unit)? = null
    private var deleteListener: ((FileBean, Int) -> Unit)? = null

    var isManage = false
    set(value) {
        if (!value) { // 非管理状态
            selectedData.clear()
            boxStateList.indices.forEach {
                boxStateList[it] = false
            }
            notifyDataSetChanged()
        }
        field = value
    }

    fun setItemClickListener(itemClicklistener: (View, FileBean, Int) -> Unit) {
        this.itemClicklistener = itemClicklistener
    }

    fun setItemLongClickListener(itemLongClickListener: (FileBean, Int) -> Boolean) {
        this.itemLongClickListener = itemLongClickListener
    }

    fun setChangeListener(changeListener: (Boolean, Int) -> Unit) {
        this.changeListener = changeListener
    }

    fun setDeleteListener(deleteListener: (FileBean, Int) -> Unit) {
        this.deleteListener = deleteListener
    }

    fun setData(data: ArrayList<FileBean>) {
        this.data.clear()
        boxStateList.clear()
        notifyDataSetChanged()
        if (this.data.addAll(data)) {
            this.data.indices.forEach { boxStateList.add(it, false) }
            this.data.indices.forEach {
                val bean = this.data[it]
                for (boxItemData in selectedData) {
                    // 一旦成立马上跳出循环
                    if (bean.filePath == boxItemData.filePath) {
                        boxStateList[it] = true
                        break
                    }
                }
            }
            notifyItemRangeChanged(0, data.size)
        }
    }

    fun deleteFile(bean: FileBean?, @NotNull position: Int) {
        data.remove(bean)
        boxStateList[position] = false
        boxStateList.removeAt(position)

        if (bean?.isFile == true) { // 删除文件
            selectedData.remove(bean)
        } else {
            //  删除该文件夹下面的所有已选中的文件
            selectedData.forEach {
                val file = File(it.filePath)
                if (!file.exists()) {
                    selectedData.remove(it)
                }
            }
        }
        notifyItemRangeRemoved(position, 1)
        changeListener?.invoke(isManage, selectedData.size)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isFile) 0 else 1
    }

    override fun onBindViewHolder(holder: FileViewHolder?, position: Int) {
        val item = getItem(position)
        holder?.apply {
            if (item.isFile) { // 文件
                setTextView(R.id.item_file_name, item.fileName)
                setTextView(R.id.item_file_date, item.date)
                setTextView(R.id.item_file_size, item.size)
                getView<SwipeMenuView>(R.id.item_file_swipeMenuView).apply {
                    isSwipeEnable = FileManageHelp.getInstance().getCanRightTouch()
                    setIos(true).isLeftSwipe = true
                }
                FileTypeManage.getInstance().loadingFile(item.filePath, getView(R.id.item_file_pic))
                holder.setViewVisibility(R.id.item_file_box_pic, !isManage)
                getView<CheckBox>(R.id.item_file_box).apply {
                    visibility = if (isManage) View.VISIBLE else View.GONE
                    isChecked = boxStateList[position]
                    setOnClickListener { boxClick(position, item) }
                }
                // boxLayout点击事件
                setOnClickListener(R.id.item_file_boxLayout,View.OnClickListener {
                    if (isManage) {
                        boxClick(position, item)
                        notifyItemChanged(position)
                    } else {
                        isManage = !isManage
                        notifyDataSetChanged()
                    }
                    changeListener?.invoke(isManage, selectedData.size)
                })
                // 删除点击事件
                setOnClickListener(R.id.item_file_delete,View.OnClickListener {
                    deleteListener?.invoke(item, position)
                })
                // 由于SwipeMenuView缘故，所以改用以下方式
                getView<View>(R.id.item_file_rootView).apply {
                    setOnClickListener {
                        if (item.isFile) itemClicklistener?.invoke(getView(R.id.item_file_pic), item, position)
                        else itemClicklistener?.invoke(getView(R.id.item_file_Jpic), item, position)
                    }
                    setOnLongClickListener {
                        itemLongClickListener?.invoke(item, position) ?: true
                    }
                }
            } else { // 文件夹
                setTextView(R.id.item_file_Jname, item.fileName)
                setOnItemClickListener(View.OnClickListener {
                    if (item.isFile) itemClicklistener?.invoke(getView(R.id.item_file_pic), item, position)
                    else itemClicklistener?.invoke(getView(R.id.item_file_Jpic), item, position)
                })
                /*setOnItemLongClickListener(View.OnLongClickListener {
                    itemLongClickListener?.invoke(item, position) ?: true
                })*/
            }
        }
    }

    private fun boxClick(position: Int, item: FileBean) {
        val b = boxStateList[position]
        if (b) { // true 表示取消选中
            selectedData.remove(item)
            boxStateList[position] = !b
            changeListener?.invoke(isManage, selectedData.size)
        } else { // false 表示点击选中
            if (selectedData.size >= FileManageHelp.getInstance().getMaxLength() && FileManageHelp.getInstance().getMaxLength() > 0) {
                context.toast(FileManageHelp.getInstance().maxLengthHintStr)
                notifyItemChanged(position)
            } else {
                selectedData.add(item)
                boxStateList[position] = !b
                changeListener?.invoke(isManage, selectedData.size)
            }
        }

    }

    fun getSelectData() = selectedData

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FileViewHolder {
        val view = when (viewType) {
            0 -> LayoutInflater.from(context).inflate(R.layout.item_files, parent, false)
            1 -> LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)
            else -> LayoutInflater.from(context).inflate(R.layout.item_files, parent, false)
        }
        return FileViewHolder(view)
    }

    private fun getItem(position: Int) = data[position]

    override fun getItemCount() = data.size
}