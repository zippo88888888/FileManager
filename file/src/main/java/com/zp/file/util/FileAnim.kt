package com.zp.file.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.View
import android.view.animation.LinearInterpolator
import com.zp.file.content.getDisplay
import com.zp.file.content.getStatusBarHeight

object FileAnim {

    private const val TYPE_Y = "y"

    /**
     * 设置View动画 --- 往上，往下动画
     * @param viewHeight     View高度
     * @param isSubtractBar 是否需要减去状态栏高度
     * @param isShow        true--显示；false--隐藏
     * @param interpolator 速度指示器
     */
    fun setTopBottomAnimView(view: View, viewHeight: Int, isSubtractBar: Boolean = true,
                             isShow: Boolean = false, interpolator: TimeInterpolator = LinearInterpolator()) {
        val endH = view.context.getDisplay()[1].toFloat()
        // 还得减去状态栏的高度
        val startH = if (isSubtractBar) endH - viewHeight - view.context.getStatusBarHeight()
        else endH - viewHeight
        val anim = if (isShow) ObjectAnimator.ofFloat(view, TYPE_Y, endH, startH)
        else ObjectAnimator.ofFloat(view, TYPE_Y, startH, endH)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (!isShow) view.visibility = View.GONE
            }
        })
        anim.interpolator = interpolator
        anim.duration = 1500
        anim.start()
        view.visibility = View.VISIBLE
    }

}