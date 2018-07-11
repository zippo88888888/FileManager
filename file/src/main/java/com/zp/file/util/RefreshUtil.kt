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

    /**
     * 设置 带 默认15dp高度的 RecyclerView 分隔线 的属性
     */
    fun setRecyclerView(recyclerView: RecyclerView,
                        orientation: Int = RecycleViewDivider.HORIZONTAL) {
        recyclerView.addItemDecoration(RecycleViewDivider(
                recyclerView.context,
                orientation,
                R.dimen.diy_divider_height,
                R.color.bgclor )
        )
    }

    /**
     * 设置通用 RecyclerView
     * @param orientation   方向
     * @param dividerColor  分隔线颜色
     * @param dividerHeight 分隔线高度
     * @param leftPadding   分隔线左边距
     * @param rightPadding  分隔线右边距
     */
    fun setRecyclerView(recyclerView: RecyclerView,
                        orientation: Int,
                        dividerColor: Int,
                        dividerHeight: Int,
                        leftPadding: Int,
                        rightPadding: Int) {

        recyclerView.addItemDecoration(RecycleViewDivider(
                recyclerView.context,
                orientation,
                dividerHeight,
                dividerColor,
                leftPadding,
                rightPadding))
    }

}