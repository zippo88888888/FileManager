package com.zp.filemanage

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zp.file.R
import com.zp.file.listener.FileImageListener
import java.io.File

/**
 * com.zp.filemanage
 * Created by ZP on 2018/9/14.
 * description:
 * version: 1.0
 */
class MyFileImageListener : FileImageListener() {

    override fun loadImage(imageView: ImageView, path: String) {
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(imageView.context)
                .load(File(path))
                .placeholder(R.drawable.file_other)
                .error(R.drawable.file_other)
                .dontAnimate()
                .into(imageView)
    }
}