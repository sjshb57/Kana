package com.sjshb57.kana.module.kana

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.sjshb57.kana.R
import com.sjshb57.kana.model.KanaRepository
import com.sjshb57.kana.model.SoundPlayer

class KanaFragment : Fragment() {

    private lateinit var gridView: GridView
    private lateinit var adapter: KanaAdapter

    private var kanaType: String = TYPE_QING
    private var isHiragana: Boolean = true

    companion object {
        const val TYPE_QING = "qing"
        const val TYPE_ZHUO = "zhuo"
        const val TYPE_AO   = "ao"

        private const val ARG_TYPE      = "arg_type"
        private const val ARG_HIRAGANA  = "arg_hiragana"

        fun newInstance(type: String, isHiragana: Boolean): KanaFragment {
            return KanaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, type)
                    putBoolean(ARG_HIRAGANA, isHiragana)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kanaType   = arguments?.getString(ARG_TYPE, TYPE_QING) ?: TYPE_QING
        isHiragana = arguments?.getBoolean(ARG_HIRAGANA, true) ?: true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_kana, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gridView = view.findViewById(R.id.gv_show_content)

        val kanaList = KanaRepository.getByType(requireContext(), kanaType)
        adapter = KanaAdapter(requireContext(), kanaList, isHiragana, kanaType)
        gridView.adapter = adapter

        // 点击
        gridView.setOnItemClickListener { _, _, position, _ ->
            val cell = adapter.getCellAt(position) ?: return@setOnItemClickListener

            when (cell.type) {
                KanaAdapter.TYPE_KANA -> {
                    // 普通假名格：播单个音
                    SoundPlayer.play(requireContext(), cell.kana!!.luoma)
                }
                KanaAdapter.TYPE_ROW -> {
                    // 行标签（あ行/か行...）：顺序播整行
                    val sounds = adapter.getRowSounds(position)
                    if (sounds.isNotEmpty()) {
                        SoundPlayer.playSequence(requireContext(), sounds)
                    }
                }
                KanaAdapter.TYPE_COL -> {
                    // 列标签（あ段/い段...）：顺序播整列
                    val sounds = adapter.getColSounds(position)
                    if (sounds.isNotEmpty()) {
                        SoundPlayer.playSequence(requireContext(), sounds)
                    }
                }
                // TYPE_CORNER / TYPE_EMPTY：不响应
            }
        }

        // 长按：震动反馈（仅普通假名格响应）
        gridView.setOnItemLongClickListener { _, itemView, position, _ ->
            val cell = adapter.getCellAt(position)
            if (cell?.type == KanaAdapter.TYPE_KANA) {
                itemView.performHapticFeedback(
                    android.view.HapticFeedbackConstants.LONG_PRESS
                )
            }
            true
        }
    }

    /** 由 KanaMainFragment 调用，切换平/片假名 */
    fun switchMode(hiragana: Boolean) {
        isHiragana = hiragana
        if (::adapter.isInitialized) {
            adapter.setMode(hiragana)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        SoundPlayer.release()
    }
}