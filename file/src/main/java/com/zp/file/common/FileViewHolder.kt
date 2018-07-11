package com.zp.file.common

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.TextView

@Suppress("UNCHECKED_CAST")
class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var array: SparseArray<View> = SparseArray()

    fun <V : View> getView(id: Int): V {
        var view: View? = array.get(id)
        if (view == null) {
            view = itemView.findViewById(id)
            array.put(id, view)
        }
        return view as V
    }

    fun setTextView(id: Int, msg: String) {
        val txtView = getView<TextView>(id)
        txtView.text = msg
    }

    fun setViewVisibility(id: Int, visibility: Int) {
        getView<View>(id).visibility = visibility
    }

    fun setViewVisibility(id: Int, isVisibility: Boolean) {
        if (isVisibility) setViewVisibility(id, View.VISIBLE) else setViewVisibility(id, View.GONE)
    }

    fun setOnClickListener(id: Int, listener: View.OnClickListener) {
        getView<View>(id).setOnClickListener(listener)
    }

    fun setOnItemClickListener(listener: View.OnClickListener) {
        itemView.setOnClickListener(listener)
    }

    fun setOnItemLongClickListener(listener: View.OnLongClickListener) {
        itemView.setOnLongClickListener(listener)
    }
}