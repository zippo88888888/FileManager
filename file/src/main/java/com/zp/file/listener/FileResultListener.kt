package com.zp.file.listener

import com.zp.file.content.FileBean

/**
 * com.zp.file.listener
 * Created by ZP on 2018/7/9.
 * description:
 * version: 1.0
 */
interface FileResultListener {

    fun resultSuccess(list: ArrayList<FileBean>?)

}