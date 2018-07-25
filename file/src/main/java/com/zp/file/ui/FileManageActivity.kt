package com.zp.file.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.zp.file.R
import com.zp.file.common.FileActivity
import com.zp.file.common.FileManage
import com.zp.file.common.FileManageHelp
import com.zp.file.content.*
import com.zp.file.util.FileManageUtil
import com.zp.file.util.FilePermissionUtil
import com.zp.file.util.RecycleViewDivider
import com.zp.file.util.RefreshUtil
import kotlinx.android.synthetic.main.activity_file_manage.*
import java.io.File

/**
 * 文件选择 2018-7-4
 *
 * description: 支持音频，视频，图片，txt，zip
 *              支持音频，视频播放，图片查看，zip解压
 *              支持单选，多选，返回上一级选中
 *              支持列表，网格，瀑布流无缝切换
 *              支持指定文件路径访问
 *              支持图片自定义查看
 *              支持音频自定义播放
 *              支持视频自定义播放
 *
 * version: 1.0
 */
class FileManageActivity : FileActivity() {

    private lateinit var fileAdapter: FileManageAdapter
    private var backList: ArrayList<String> = ArrayList()
    private lateinit var filePathAdapter: FilePathAdapter

    private var barShow = false

    private var sortCheckedID = R.id.sort_by_default
    private var sequenceCheckedId = R.id.sequence_asc

    override fun getContentView() = R.layout.activity_file_manage

    override fun getFileManage() = FileManageHelp.getInstance()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.file_menu, menu)
        return true
    }

    private fun setMenuState() {
        file_manage_bar.menu.apply {
            findItem(R.id.menu_file_down).isVisible = barShow
            findItem(R.id.menu_file_px).isVisible = !barShow
            findItem(R.id.menu_file_show).isVisible = !barShow
            findItem(R.id.menu_file_hidden).isVisible = !barShow
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        setSupportActionBar(file_manage_bar)
        file_manage_bar.apply {
            setOnMenuItemClickListener { menu -> menuItemClick(menu) }
            setNavigationIcon(R.drawable.file_back)
            title = "文件管理"
            setNavigationOnClickListener { onBackPressed() }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkHasPermission() else initAll()
    }

    private fun menuItemClick(menu: MenuItem?): Boolean {
        when (menu?.itemId) {
            R.id.menu_file_down -> {
                val list = fileAdapter.getSelectData()
                if (list.size <= 0) {
                    file_manage_bar.title = "文件管理"
                    fileAdapter.isManage = false
                    barShow = false
                    setMenuState()
                } else {
                    FileManageHelp.getInstance().fileResultListener?.resultSuccess(fileAdapter.getSelectData())
                    finish()
                }
            }
            R.id.menu_file_px -> showSortDialog()
            R.id.menu_file_show -> {
                menu.isChecked = true
                FileManageHelp.getInstance().setShowHiddenFile(true)
                FileManageUtil.getInstance().getList(getThisFilePath(), { list -> fileAdapter.setData(list) })
            }
            R.id.menu_file_hidden -> {
                menu.isChecked = true
                FileManageHelp.getInstance().setShowHiddenFile(false)
                FileManageUtil.getInstance().getList(getThisFilePath(), { list -> fileAdapter.setData(list) })
            }
        }
        return true
    }

    /** 返回当前的路径 */
    private fun getThisFilePath() = if (backList.isEmpty()) null else backList[backList.size - 1]

    private fun initAll() {
        file_manage_refreshLayout.apply {
            setColorSchemeColors(getColorById(R.color.file_manager_tool_bar_color))
            setDistanceToTriggerSync(400)
            setOnRefreshListener {
                FileManageUtil.getInstance().getList(getThisFilePath(), { list ->
                    setEmptyByListSize(list.size)
                    fileAdapter.setData(list)
                    isRefreshing = false
                })
            }
        }
        val filePath = intent.getStringExtra("filePath")
        if (filePath != null) {
            FileManageUtil.getInstance().filePath = filePath
        }
        backList.add(FileManageUtil.getInstance().filePath)
        fileAdapter = FileManageAdapter(this).run {
            setItemClickListener { view, bean, _ ->
                if (bean.isFile) {
                    // 打开文件
                    manage?.openFile(null, bean.filePath, view, this@FileManageActivity)
                } else {
                    FileManageUtil.getInstance().getList(bean.filePath, { list ->
                        setEmptyByListSize(list.size)
                        log("进入--->>>${bean.filePath}")
                        backList.add(bean.filePath)
                        fileAdapter.setData(list)
                        filePathAdapter.addData(bean, { isSuccess ->
                            // 滚动至底部
                            if (isSuccess) file_manage_path_recyclerView.scrollToPosition(filePathAdapter.itemCount - 1)
                        })
                    })
                }
            }
            setItemLongClickListener { bean, position ->
                createCallDialog(bean, position)
            }
            setChangeListener { isManage, size ->
                if (isManage) { // 管理状态
                    if (barShow) {
                        file_manage_bar.title = "已选中${size}个文件"
                        return@setChangeListener
                    }
                    barShow = true
                    file_manage_bar.title = "已选中0个文件"
                    setMenuState()
                }
            }
            setDeleteListener { bean, position ->
                manage?.deleteFile(this@FileManageActivity, bean.filePath, { isSuccess ->
                    if (isSuccess) fileAdapter.deleteFile(bean, position)
                    else toast("删除失败")
                })
            }
            this
        }
        filePathAdapter = FilePathAdapter(this)
        initRecyclerView()
    }

    private fun showSortDialog() {
        checkFragmentByTag(SORT_DIALOG_TAG)
        SortDialog.newInstance(sortCheckedID, sequenceCheckedId).apply {
            setCheckedChangedListener { sortCheckedID, sequenceCheckedId ->
                this@FileManageActivity.sortCheckedID = sortCheckedID
                this@FileManageActivity.sequenceCheckedId = sequenceCheckedId
                val sortordByWhat = when (sortCheckedID) {
                    R.id.sort_by_default -> FileManageHelp.BY_DEFAULT
                    R.id.sort_by_name -> FileManageHelp.BY_NAME
                    R.id.sort_by_date -> FileManageHelp.BY_DATE
                    R.id.sort_by_size -> FileManageHelp.BY_SIZE
                    else -> FileManageHelp.BY_DEFAULT
                }
                val sortord = when (sequenceCheckedId) {
                    R.id.sequence_asc -> FileManageHelp.ASC
                    R.id.sequence_desc -> FileManageHelp.DESC
                    else -> FileManageHelp.ASC
                }
                FileManageHelp.getInstance().apply {
                    setSortordByWhat(sortordByWhat)
                    setSortord(sortord)
                }
                FileManageUtil.getInstance().getList(getThisFilePath(), { list ->
                    fileAdapter.setData(list)
                })
            }
            show(supportFragmentManager, SORT_DIALOG_TAG)
        }
    }

    private fun createCallDialog(bean: FileBean, position: Int): Boolean {
        AlertDialog.Builder(this).apply {
            setTitle("请选择")
            setItems(arrayOf(/*"复制", "剪切", "粘贴",*/ "删除文件", "查看详情"), { dialog, which ->
                jumpByWhich(bean, which, position)
                dialog.dismiss()
            })
            setPositiveButton("取消", { dialog, _ -> dialog.dismiss() })
            show()
        }
        return true
    }

    private fun jumpByWhich(bean: FileBean, which: Int, position: Int) {
        when (which) {
            0 -> manage?.deleteFile(this, bean.filePath, { isSuccess ->
                if (isSuccess) fileAdapter.deleteFile(bean, position)
                else toast("暂不支持删除多个文件，删除失败")
            })
            1 -> manage?.infoFile(null, bean, this)

            /*0 -> manage?.copyFile(bean.filePath, this)
            1 -> manage?.cutFile(bean.filePath, this)
            2 -> manage?.pasteFile(bean.filePath, this)
            3 -> manage?.deleteFile(this, bean.filePath, { isSuccess ->
                if (isSuccess) fileAdapter.deleteFile(bean, position)
                else toast("暂不支持删除多个文件，删除失败")
            })
            4 -> manage?.infoFile(null, bean.filePath, this)*/
        }
    }

    private fun initRecyclerView() {
        file_manage_recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FileManageActivity)
            adapter = fileAdapter
        }
        RefreshUtil.setRecyclerViewLine(file_manage_recyclerView, RecycleViewDivider.HORIZONTAL, true)
        FileManageUtil.getInstance().getList(null, { list -> fileAdapter.setData(list) })

        file_manage_path_recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FileManageActivity).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = filePathAdapter
        }
    }

    private fun setEmptyByListSize(size: Int) {
        file_manage_empty.visibility = if (size <= 0) View.VISIBLE
        else View.GONE
    }

    override fun telActivity(oldPath: String, outZipPath: String?) {
        val outPath = outZipPath ?: SD_ROOT
        manage?.zipFile(oldPath, outPath, this)
    }

    override fun onBackPressed() {
        val path = getThisFilePath()
        if (path == FileManageUtil.getInstance().filePath) { // 根目录
            if (barShow) { // 存在编辑状态
                file_manage_bar.title = "文件管理"
                fileAdapter.isManage = false
                barShow = false
                setMenuState()
            } else {
                super.onBackPressed()
            }
        } else { // 返回上一级
            val parentPath = File(path).parent
            log("回退--->>>$parentPath")
            FileManageUtil.getInstance().getList(parentPath ?: null, { list ->
                setEmptyByListSize(list.size)
                fileAdapter.setData(list)
                backList.removeAt(backList.size - 1)
                filePathAdapter.back()
            })
        }
    }

    private fun checkHasPermission() {
        val hasPermission = FilePermissionUtil.hasPermission(this, FilePermissionUtil.WRITE_EXTERNAL_STORAGE)
        if (hasPermission) {
            FilePermissionUtil.requestPermission(this, FilePermissionUtil.WRITE_EXTERNAL_CODE, FilePermissionUtil.WRITE_EXTERNAL_STORAGE)
        } else {
            initAll()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FilePermissionUtil.WRITE_EXTERNAL_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) initAll()
            else toast("权限申请失败")
        }
    }
}