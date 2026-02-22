package com.sjshb57.kana.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.sjshb57.kana.R

class SwitchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    interface OnTabClickListener {
        fun onTabClick(isLeft: Boolean)
    }

    private val btnLeft: ColorChangeButton
    private val btnRight: ColorChangeButton
    private var listener: OnTabClickListener? = null

    var isLeftSelected: Boolean = true
        private set

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_switch, this, true)
        btnLeft  = findViewById(R.id.btn_switch_left)
        btnRight = findViewById(R.id.btn_switch_right)

        // 默认左边（平假名）选中
        setLeftSelected(true)

        btnLeft.setOnClickListener {
            if (!isLeftSelected) setLeftSelected(true)
        }
        btnRight.setOnClickListener {
            if (isLeftSelected) setLeftSelected(false)
        }
    }

    fun setLeftSelected(left: Boolean) {
        isLeftSelected = left
        // 原版用的是 setSelected，ColorChangeButton 里用 isSelected 状态驱动颜色
        btnLeft.isSelected  = left
        btnRight.isSelected = !left
        // 触发颜色刷新
        btnLeft.updateAppearance()
        btnRight.updateAppearance()
        listener?.onTabClick(left)
    }

    fun setOnTabClickListener(l: OnTabClickListener) {
        listener = l
    }
}