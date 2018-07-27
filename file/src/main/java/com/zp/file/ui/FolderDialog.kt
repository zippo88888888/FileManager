package com.zp.file.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zp.file.R
import com.zp.file.content.COPY_TYPE
import com.zp.file.content.FileBean
import com.zp.file.content.getDisplay
import com.zp.file.content.log
import com.zp.file.listener.TelActivityListener
import com.zp.file.util.FileManageUtil
import com.zp.file.util.RecycleViewDivider
import com.zp.file.util.RefreshUtil
import kotlinx.android.synthetic.main.dialog_folder_layout.*
import java.io.File

class FolderDialog : DialogFragment() {

    companion object {
        fun newInstance(filePath: String, type: Int) = FolderDialog().apply {
            arguments = Bundle().apply {
                putString("filePath", filePath)
                putInt("type", type)
            }
        }
    }

    var telActivityListener: TelActivityListener? = null
    private var folderAdapter: FolderAdapter? = null
    private var filePathAdapter: FilePathAdapter? = null
    private var backList: ArrayList<String> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater?.inflate(R.layout.dialog_folder_layout, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?) = BottomSheetDialog(context)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN)
                onBackPressed()
            else false
        }
        folderAdapter = FolderAdapter(context)
        filePathAdapter = FilePathAdapter(context)
        folderAdapter?.setItemClickListener { bean, _ ->
            if (!bean.isFile) {
                openFile(bean)
            }
        }
        initRecyclerView()
        RefreshUtil.setRecyclerViewLine(dialog_folder_recyclerView, RecycleViewDivider.HORIZONTAL, true)
        FileManageUtil.getInstance().getList(null, { list -> folderAdapter?.setData(list) }, true)
        dialog_folder_cancel.setOnClickListener { dismiss() }
        dialog_folder_down.setOnClickListener {
            telActivityListener?.telActivity(
                    arguments.getString("filePath"), getThisFilePath(),
                    arguments.getInt("type", COPY_TYPE))
            dismiss()
        }
    }

    private fun openFile(bean: FileBean) {
        FileManageUtil.getInstance().getList(bean.filePath, { list ->
            log("进入--->>>${bean.filePath}")
            backList.add(bean.filePath)
            folderAdapter?.setData(list)
            filePathAdapter?.addData(bean, { isSuccess ->
                if (isSuccess) {
                    dialog_folder_path_recyclerView.scrollToPosition((filePathAdapter?.itemCount ?: 1) - 1)
                    setDialogWH()
                }
            })
        }, true)
    }

    private fun initRecyclerView() {
        dialog_folder_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = folderAdapter
            isNestedScrollingEnabled = false
        }
        dialog_folder_path_recyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            isNestedScrollingEnabled = false
            adapter = filePathAdapter
        }
    }

    private fun onBackPressed() : Boolean {
        val path = getThisFilePath()
        if (path != FileManageUtil.getInstance().filePath && path != null) {
            val parentPath = File(path).parent
            FileManageUtil.getInstance().getList(parentPath ?: null, { list ->
                folderAdapter?.setData(list)
                backList.removeAt(backList.size - 1)
                filePathAdapter?.back()
                setDialogWH()
            }, true)
            return true
        }
        return false
    }

    private fun getThisFilePath() = if (backList.isEmpty()) null else backList[backList.size - 1]

    private fun setDialogWH() {
        val display = context.getDisplay()
        dialog.window.setLayout(display[0], display[1])
        dialog_sort_rootView.layoutParams =
                FrameLayout.LayoutParams(display[0], display[1])
    }

    override fun onDestroyView() {
        log("onDestroyView")
        backList.clear()
        telActivityListener = null
        super.onDestroyView()
    }
}