package com.sjshb57.kana.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 禁止手势左右滑动的 ViewPager
 * 用于 MainActivity 底部 Tab 切换，只允许代码控制切换页面
 */
class NonSwipePager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = false

    override fun onTouchEvent(ev: MotionEvent): Boolean = false
}