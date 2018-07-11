package com.zp.file.listener

import com.zp.file.content.FileBean

interface FileResultListener {

    fun resultSuccess(list: ArrayList<FileBean>?)

}