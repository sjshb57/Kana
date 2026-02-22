package com.sjshb57.kana.module.kana

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sjshb57.kana.R
import com.sjshb57.kana.view.PagerSlidingTabStrip
import com.sjshb57.kana.view.SwitchView

class KanaMainFragment : Fragment() {

    private lateinit var switchView: SwitchView
    private lateinit var tabStrip: PagerSlidingTabStrip
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: KanaPagerAdapter

    private var isHiragana = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_kana_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar   = view.findViewById<Toolbar>(R.id.kana_main_toolbar)
        switchView    = view.findViewById(R.id.kana_main_switch_view)
        tabStrip      = view.findViewById(R.id.kana_main_pager_tab)
        viewPager     = view.findViewById(R.id.kana_main_view_pager)

        // 处理状态栏高度，把 Toolbar 顶部 padding 设为状态栏高度
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.setPadding(
                v.paddingLeft,
                statusBarHeight,
                v.paddingRight,
                v.paddingBottom
            )
            // 同步撑高 Toolbar 自身高度
            val lp = v.layoutParams
            lp.height = resources.getDimensionPixelSize(R.dimen.toolbar_height) + statusBarHeight
            v.layoutParams = lp
            insets
        }

        pagerAdapter = KanaPagerAdapter(childFragmentManager, isHiragana)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 2
        tabStrip.setViewPager(viewPager)

        switchView.setOnTabClickListener(object : SwitchView.OnTabClickListener {
            override fun onTabClick(isLeft: Boolean) {
                // isLeft=true 是平假名，isLeft=false 是片假名
                isHiragana = isLeft
                pagerAdapter.switchAllMode(isLeft)
            }
        })
    }

    // -----------------------------------------------------------------------
    inner class KanaPagerAdapter(
        fm: FragmentManager,
        private var isHiragana: Boolean
    ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragments = listOf(
            KanaFragment.newInstance(KanaFragment.TYPE_QING, isHiragana),
            KanaFragment.newInstance(KanaFragment.TYPE_ZHUO, isHiragana),
            KanaFragment.newInstance(KanaFragment.TYPE_AO,   isHiragana)
        )

        private val titles = listOf("清音", "浊音", "拗音")

        override fun getCount(): Int = fragments.size
        override fun getItem(position: Int): Fragment = fragments[position]
        override fun getPageTitle(position: Int): CharSequence = titles[position]

        fun switchAllMode(hiragana: Boolean) {
            fragments.forEach { it.switchMode(hiragana) }
        }
    }
}