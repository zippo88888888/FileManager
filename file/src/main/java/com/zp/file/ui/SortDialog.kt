package com.zp.file.ui

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.zp.file.R
import kotlinx.android.synthetic.main.dialog_sort_layout.*

class SortDialog : DialogFragment(), RadioGroup.OnCheckedChangeListener {

    companion object {
        fun newInstance(sortCheckedID: Int, sequenceCheckedId: Int) = SortDialog().apply {
            arguments = Bundle().apply {
                putInt("sortCheckedID", sortCheckedID)
                putInt("sequenceCheckedId", sequenceCheckedId)
            }
        }
    }

    private var sortCheckedID = R.id.sort_by_default
    private var sequenceCheckedId = R.id.sequence_asc

    private var checkedChangedListener: ((Int, Int) -> Unit)? = null
    fun setCheckedChangedListener(checkedChangedListener: (Int, Int) -> Unit) {
        this.checkedChangedListener = checkedChangedListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater?.inflate(R.layout.dialog_sort_layout, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?) = Dialog(context, R.style.Common_Dialog).apply {
        window.setGravity(Gravity.CENTER)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        sortCheckedID = arguments.getInt("sortCheckedID", R.id.sort_by_default)
        sequenceCheckedId = arguments.getInt("sequenceCheckedId", R.id.sequence_asc)
        check()
        when (sortCheckedID) {
            R.id.sort_by_default -> sort_by_default.isChecked = true
            R.id.sort_by_name -> sort_by_name.isChecked = true
            R.id.sort_by_date -> sort_by_date.isChecked = true
            R.id.sort_by_size -> sort_by_size.isChecked = true
            else -> sort_by_default.isChecked = true
        }
        when (sequenceCheckedId) {
            R.id.sequence_asc -> sequence_asc.isChecked = true
            R.id.sequence_desc -> sequence_desc.isChecked = true
            else -> sequence_asc.isChecked = true
        }
        sortGroup.setOnCheckedChangeListener(this)
        sequenceGroup.setOnCheckedChangeListener(this)
        dialog_sort_cancel.setOnClickListener { dismiss() }
        dialog_sort_down.setOnClickListener {
            checkedChangedListener?.invoke(sortCheckedID, sequenceCheckedId)
            dismiss()
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (group?.id == R.id.sortGroup) { // 方式
            sortCheckedID = checkedId
            check()
        } else { // 顺序
            sequenceCheckedId = checkedId
        }
    }

    private fun check() {
        sequenceLayout.visibility = if (sortCheckedID == R.id.sort_by_default) View.GONE else View.VISIBLE
    }
}