package com.zp.file.listener

import java.io.File
import java.io.FileFilter

/**
 * 过滤规则
 * @param fileArray 规则
 * @param isOnlyFolder 是否只需要显示文件夹
 */
class LowFileFilter(private var fileArray: Array<String>?, private var isOnlyFolder: Boolean) : FileFilter {

    override fun accept(file: File): Boolean {
        if (isOnlyFolder) { // 只显示文件夹
            return file.isDirectory
        }
        if (file.isDirectory) { // 文件夹直接返回
            return true
        }
        if (fileArray != null && fileArray!!.isNotEmpty()) {
            fileArray?.forEach {
                if (file.name.endsWith(it.toLowerCase()) || file.name.endsWith(it.toUpperCase())) {
                    return true
                }
            }
        } else {
            return true
        }
        return false
    }
}