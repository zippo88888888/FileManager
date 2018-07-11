package com.zp.file.listener

import java.io.File
import java.io.FileFilter

/**
 * com.zp.file.listener
 * Created by ZP on 2018/7/6.
 * description:
 * version: 1.0
 */
class LowFileFilter(private var fileArray: Array<String>?) : FileFilter {

    override fun accept(file: File): Boolean {
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