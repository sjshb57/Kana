package com.sjshb57.kana.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.sjshb57.kana.R

class ColorChangeButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var normalBgColor:    Int = Color.WHITE
    private var selectedBgColor:  Int = Color.parseColor("#ff5e5e")
    private var normalTxtColor:   Int = Color.parseColor("#ff5e5e")
    private var selectedTxtColor: Int = Color.WHITE
    private var strokeColor:      Int = Color.parseColor("#ff5e5e")
    private var strokeWidth:      Int = 2
    private var cornerTL: Float = 0f
    private var cornerTR: Float = 0f
    private var cornerBL: Float = 0f
    private var cornerBR: Float = 0f

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ColorChangeButton)
        try {
            normalBgColor   = ta.getColor(R.styleable.ColorChangeButton_normalBackgroundColor, normalBgColor)
            selectedBgColor = ta.getColor(R.styleable.ColorChangeButton_selectedBackgroundColor, selectedBgColor)
            normalTxtColor  = ta.getColor(R.styleable.ColorChangeButton_normalTextColor, normalTxtColor)
            selectedTxtColor = ta.getColor(R.styleable.ColorChangeButton_selectedTextColor, selectedTxtColor)
            strokeColor     = ta.getColor(R.styleable.ColorChangeButton_backgroundStrokeColor, strokeColor)
            strokeWidth     = ta.getDimensionPixelSize(R.styleable.ColorChangeButton_backgroundStrokeWidth, strokeWidth)
            cornerTL = ta.getDimension(R.styleable.ColorChangeButton_backgroundTopLeftCorner, 0f)
            cornerTR = ta.getDimension(R.styleable.ColorChangeButton_backgroundTopRightCorner, 0f)
            cornerBL = ta.getDimension(R.styleable.ColorChangeButton_backgroundBottomLeftCorner, 0f)
            cornerBR = ta.getDimension(R.styleable.ColorChangeButton_backgroundBottomRightCorner, 0f)
        } finally {
            ta.recycle()
        }
        updateAppearance()
    }

    // 原版直接用 setSelected 驱动外观
    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        updateAppearance()
    }

    fun updateAppearance() {
        val bgColor  = if (isSelected) selectedBgColor else normalBgColor
        val txtColor = if (isSelected) selectedTxtColor else normalTxtColor
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(bgColor)
            setStroke(strokeWidth, strokeColor)
            cornerRadii = floatArrayOf(
                cornerTL, cornerTL, cornerTR, cornerTR,
                cornerBR, cornerBR, cornerBL, cornerBL
            )
        }
        setTextColor(txtColor)
    }
}