package com.zp.file.util

import android.support.v7.widget.RecyclerView
import com.zp.file.R

object RefreshUtil {

    /**
     * 设置通用 RecyclerView 带默认分隔线的属性
     * @param isPadding 是否显示左右边距
     */
    @JvmStatic fun setRecyclerViewLine(recyclerView: RecyclerView,
                            orientation: Int = RecycleViewDivider.HORIZONTAL,
                            isPadding: Boolean = true) {
        if (isPadding) {
            recyclerView.addItemDecoration(RecycleViewDivider(
                    recyclerView.context,
                    orientation,
                    0,
                    0,
                    R.dimen.default_divider_padding,
                    R.dimen.default_divider_padding)
            )
        } else {
            recyclerView.addItemDecoration(RecycleViewDivider(recyclerView.context, orientation))
        }
    }

}