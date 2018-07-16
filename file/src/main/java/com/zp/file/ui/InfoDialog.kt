package com.zp.file.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zp.file.R
import com.zp.file.content.FileBean
import com.zp.file.content.FileInfoBean
import com.zp.file.content.getDisplay
import com.zp.file.type.*
import com.zp.file.util.FileManageUtil
import kotlinx.android.synthetic.main.dialog_info_layout.*
import java.lang.ref.WeakReference

class InfoDialog : DialogFragment(), Runnable {

    companion object {
        fun newInstance(bean: FileBean) = InfoDialog().apply {
            arguments = Bundle().apply { putSerializable("fileBean", bean) }
        }
    }

    private var handler: InfoHandler? = null
    private lateinit var thread: Thread
    private var filePath = ""
    private var fileType: FileType? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater?.inflate(R.layout.dialog_info_layout, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?) = Dialog(context, R.style.Common_Dialog).apply {
        window.setGravity(Gravity.CENTER)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val bean = arguments.getSerializable("fileBean") as FileBean
        filePath = bean.filePath
        fileType = FileTypeManage.getInstance().getFileType(bean.filePath)
        handler = InfoHandler(this)
        thread = Thread(this)
        thread.start()

        dialog_info_fileName.text = bean.fileName
        dialog_info_fileType.text = bean.filePath.run {
            substring(lastIndexOf(".") + 1, length)
        }
        dialog_info_fileDate.text = bean.date
        dialog_info_fileSize.text = bean.size
        dialog_info_filePath.text = bean.filePath

        dialog_info_moreBox.setOnClickListener {
            if (dialog_info_moreBox.isChecked) {
                dialog_info_moreLayout.visibility = View.VISIBLE
            } else {
                dialog_info_moreLayout.visibility = View.GONE
            }
        }
        when (fileType) {
            is ImageType -> {
                dialog_info_moreBox.visibility = View.VISIBLE
                dialog_info_fileDurationLayout.visibility = View.GONE
                dialog_info_fileOther.text = "无"
                var map = BitmapFactory.decodeFile(filePath)
                dialog_info_fileFBL.text = "${map.width} * ${map.height}"
                map.recycle()
                map = null
            }
            is AudioType -> {
                dialog_info_moreBox.visibility = View.VISIBLE
                dialog_info_fileFBLLayout.visibility = View.GONE
                dialog_info_fileOther.text = "无"
            }
            is VideoType -> {
                dialog_info_moreBox.visibility = View.VISIBLE
                dialog_info_fileOther.text = "无"
            }
            else -> {
                dialog_info_moreBox.visibility = View.GONE
                dialog_info_moreLayout.visibility = View.GONE
            }
        }
        dialog_info_down.setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        val width = context.getDisplay()[0] * 0.88f
        dialog.window.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(this)
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    override fun run() {
        if (fileType !is AudioType && fileType !is VideoType) return
        val msg = Message()
        msg.what = 0
        msg.obj = FileManageUtil.getInstance().getMultimediaInfo(filePath, fileType is VideoType)
        handler?.sendMessage(msg)
    }

    class InfoHandler(dialog: InfoDialog) : Handler() {
        private val week: WeakReference<InfoDialog> by lazy {
            WeakReference<InfoDialog>(dialog)
        }

        override fun handleMessage(msg: Message?) {
            if (msg?.what == 0) {
                val bean = msg.obj as FileInfoBean
                when (week.get()?.fileType) {
                    is AudioType -> {
                        week.get()?.dialog_info_fileDuration?.text = bean.duration
                    }
                    is VideoType -> {
                        week.get()?.dialog_info_fileDuration?.text = bean.duration
                        week.get()?.dialog_info_fileFBL?.text = "${bean.width} * ${bean.height}"
                    }
                }
            }
        }
    }

}