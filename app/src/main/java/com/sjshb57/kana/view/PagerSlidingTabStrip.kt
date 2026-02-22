package com.sjshb57.kana.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.sjshb57.kana.R

/**
 * 仿原APK的 PagerSlidingTabStrip
 * 功能：与 ViewPager 联动，显示清音/浊音/拗音三个Tab，底部有滑动指示线
 */
class PagerSlidingTabStrip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : HorizontalScrollView(context, attrs, defStyle) {

    // ---- 可配置属性 ----
    var indicatorColor: Int = Color.parseColor("#ff5e5e")
    var indicatorHeight: Int = dp2px(3f)
    var underlineColor: Int = Color.parseColor("#E0E0E0")
    var underlineHeight: Int = dp2px(1f)
    var dividerColor: Int = Color.TRANSPARENT
    var tabPaddingLeftRight: Int = dp2px(16f)
    var shouldExpand: Boolean = true  // Tab平均分配宽度
    var textAllCaps: Boolean = false

    private val tabsContainer: LinearLayout
    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val underlinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var viewPager: ViewPager? = null
    private var currentPosition: Int = 0
    private var currentPositionOffset: Float = 0f

    init {
        isHorizontalScrollBarEnabled = false
        isFillViewport = true

        tabsContainer = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        addView(tabsContainer)

        // 读取自定义属性
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip)
        try {
            indicatorColor     = ta.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor)
            indicatorHeight    = ta.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight)
            underlineColor     = ta.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor)
            underlineHeight    = ta.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight)
            dividerColor       = ta.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor)
            tabPaddingLeftRight = ta.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPaddingLeftRight)
            shouldExpand       = ta.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand)
            textAllCaps        = ta.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps)
        } finally {
            ta.recycle()
        }

        setWillNotDraw(false)
    }

    /**
     * 绑定 ViewPager，调用后自动根据 adapter 的 getPageTitle 生成 Tab
     */
    fun setViewPager(pager: ViewPager) {
        viewPager = pager
        notifyDataSetChanged()
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(pos: Int, offset: Float, offsetPixels: Int) {
                currentPosition = pos
                currentPositionOffset = offset
                invalidate()
            }
            override fun onPageSelected(pos: Int) {
                currentPosition = pos
                updateTabStyles()
                scrollToChild(pos)
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun notifyDataSetChanged() {
        val adapter = viewPager?.adapter ?: return
        tabsContainer.removeAllViews()

        for (i in 0 until adapter.count) {
            val title = adapter.getPageTitle(i)?.toString() ?: i.toString()
            addTab(i, title)
        }
        updateTabStyles()
    }

    private fun addTab(position: Int, title: String) {
        val tab = TextView(context).apply {
            text = if (textAllCaps) title.uppercase() else title
            gravity = Gravity.CENTER
            setSingleLine()
            setPadding(tabPaddingLeftRight, 0, tabPaddingLeftRight, 0)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        }

        val params = if (shouldExpand) {
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        } else {
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        tab.layoutParams = params

        tab.setOnClickListener { viewPager?.currentItem = position }
        tabsContainer.addView(tab)
    }

    private fun updateTabStyles() {
        for (i in 0 until tabsContainer.childCount) {
            val tab = tabsContainer.getChildAt(i) as? TextView ?: continue
            if (i == currentPosition) {
                tab.setTextColor(indicatorColor)
                tab.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                tab.setTextColor(Color.parseColor("#666666"))
                tab.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }

    private fun scrollToChild(position: Int) {
        val tabView = tabsContainer.getChildAt(position) ?: return
        val scrollX = tabView.left - (width / 2) + (tabView.width / 2)
        smoothScrollTo(scrollX.coerceAtLeast(0), 0)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (tabsContainer.childCount == 0) return

        val height = this.height

        // 画底部横线
        underlinePaint.color = underlineColor
        canvas.drawRect(0f, (height - underlineHeight).toFloat(), tabsContainer.width.toFloat(), height.toFloat(), underlinePaint)

        // 画选中指示线
        val currentTab = tabsContainer.getChildAt(currentPosition) ?: return
        var lineLeft  = currentTab.left.toFloat()
        var lineRight = currentTab.right.toFloat()

        if (currentPositionOffset > 0f && currentPosition < tabsContainer.childCount - 1) {
            val nextTab = tabsContainer.getChildAt(currentPosition + 1)
            lineLeft  += currentPositionOffset * (nextTab.left  - currentTab.left)
            lineRight += currentPositionOffset * (nextTab.right - currentTab.right)
        }

        indicatorPaint.color = indicatorColor
        canvas.drawRect(lineLeft, (height - indicatorHeight).toFloat(), lineRight, height.toFloat(), indicatorPaint)
    }

    private fun dp2px(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
}