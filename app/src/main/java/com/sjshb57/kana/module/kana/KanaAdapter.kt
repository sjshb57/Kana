package com.sjshb57.kana.module.kana

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.sjshb57.kana.R
import com.sjshb57.kana.model.Kana

data class KanaCell(
    val type: Int,
    val kana: Kana? = null,
    val label: String = ""
)

class KanaAdapter(
    private val context: Context,
    kanaList: List<Kana>,
    private var isHiragana: Boolean,
    private val kanaType: String
) : BaseAdapter() {

    companion object {
        const val TYPE_CORNER = 0
        const val TYPE_COL    = 1
        const val TYPE_ROW    = 2
        const val TYPE_KANA   = 3
        const val TYPE_EMPTY  = 4

        private val COL_LABELS     = listOf("あ段", "い段", "う段", "え段", "お段")
        private val AO_COL_LABELS  = listOf("ゃ段", "ゅ段", "ょ段")
    }

    private var cells: List<KanaCell> = buildCells(kanaList)

    // -----------------------------------------------------------------------
    // 构建格子列表
    // -----------------------------------------------------------------------

    private fun buildCells(kanaList: List<Kana>): List<KanaCell> {
        val result = mutableListOf<KanaCell>()
        when (kanaType) {
            "qing" -> buildQingCells(kanaList, result)
            "zhuo" -> buildZhuoCells(kanaList, result)
            "ao"   -> buildAoCells(kanaList, result)
        }
        return result
    }

    private fun buildQingCells(list: List<Kana>, result: MutableList<KanaCell>) {
        // 第0行：左上角 + 5个列标签
        result.add(KanaCell(TYPE_CORNER))
        COL_LABELS.forEach { result.add(KanaCell(TYPE_COL, label = it)) }

        val table: List<Pair<String, List<Kana?>>> = listOf(
            "あ行" to listOf(list[0],  list[1],  list[2],  list[3],  list[4]),
            "か行" to listOf(list[5],  list[6],  list[7],  list[8],  list[9]),
            "さ行" to listOf(list[10], list[11], list[12], list[13], list[14]),
            "た行" to listOf(list[15], list[16], list[17], list[18], list[19]),
            "な行" to listOf(list[20], list[21], list[22], list[23], list[24]),
            "は行" to listOf(list[25], list[26], list[27], list[28], list[29]),
            "ま行" to listOf(list[30], list[31], list[32], list[33], list[34]),
            "や行" to listOf(list[35], null,     list[37], null,     list[39]),
            "ら行" to listOf(list[40], list[41], list[42], list[43], list[44]),
            "わ行" to listOf(list[45], null,     null,     null,     list[49]),
            "ん行" to listOf(list[50], null,     null,     null,     null),
        )

        table.forEach { (rowLabel, row) ->
            result.add(KanaCell(TYPE_ROW, label = rowLabel))
            row.forEach { kana ->
                result.add(
                    if (kana != null) KanaCell(TYPE_KANA, kana = kana)
                    else KanaCell(TYPE_EMPTY)
                )
            }
        }
    }

    private fun buildZhuoCells(list: List<Kana>, result: MutableList<KanaCell>) {
        result.add(KanaCell(TYPE_CORNER))
        COL_LABELS.forEach { result.add(KanaCell(TYPE_COL, label = it)) }

        val table: List<Pair<String, List<Kana?>>> = listOf(
            "が行" to listOf(list[0],  list[1],  list[2],  list[3],  list[4]),
            "ざ行" to listOf(list[5],  list[6],  list[7],  list[8],  list[9]),
            "だ行" to listOf(list[10], list[11], list[12], list[13], list[14]),
            "ば行" to listOf(list[15], list[16], list[17], list[18], list[19]),
            "ぱ行" to listOf(list[20], list[21], list[22], list[23], list[24]),
        )

        table.forEach { (rowLabel, row) ->
            result.add(KanaCell(TYPE_ROW, label = rowLabel))
            row.forEach { kana ->
                result.add(
                    if (kana != null) KanaCell(TYPE_KANA, kana = kana)
                    else KanaCell(TYPE_EMPTY)
                )
            }
        }
    }

    private fun buildAoCells(list: List<Kana>, result: MutableList<KanaCell>) {
        // 拗音每行3个（ゃゅょ），列标签换成3个
        result.add(KanaCell(TYPE_CORNER))
        AO_COL_LABELS.forEach { result.add(KanaCell(TYPE_COL, label = it)) }
        // 第0行共4格：左上角 + 3个列标签 + 2个空位，凑够6列
        result.add(KanaCell(TYPE_EMPTY))
        result.add(KanaCell(TYPE_EMPTY))

        val rowLabels = listOf(
            "きゃ行", "しゃ行", "ちゃ行", "にゃ行", "ひゃ行",
            "みゃ行", "りゃ行", "ぎゃ行", "じゃ行", "びゃ行", "ぴゃ行"
        )

        rowLabels.forEachIndexed { i, label ->
            val base = i * 3
            result.add(KanaCell(TYPE_ROW, label = label))
            result.add(KanaCell(TYPE_KANA, kana = list[base]))
            result.add(KanaCell(TYPE_KANA, kana = list[base + 1]))
            result.add(KanaCell(TYPE_KANA, kana = list[base + 2]))
            result.add(KanaCell(TYPE_EMPTY))
            result.add(KanaCell(TYPE_EMPTY))
        }
    }

    // -----------------------------------------------------------------------
    // BaseAdapter 实现
    // -----------------------------------------------------------------------

    override fun getCount(): Int = cells.size
    override fun getItem(position: Int): Any = cells[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getViewTypeCount(): Int = 5
    override fun getItemViewType(position: Int): Int = cells.getOrNull(position)?.type ?: TYPE_EMPTY

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cell = cells[position]
        val inflater = LayoutInflater.from(context)

        return when (cell.type) {
            TYPE_CORNER -> {
                convertView ?: inflater.inflate(R.layout.item_grid_view_all, parent, false)
            }
            TYPE_COL, TYPE_ROW -> {
                val v = convertView ?: inflater.inflate(R.layout.item_grid_view_row, parent, false)
                v.findViewById<TextView>(R.id.tv_grid_label).text = cell.label
                v
            }
            TYPE_KANA -> {
                val v = convertView ?: inflater.inflate(R.layout.item_grid_view, parent, false)
                val tvKana  = v.findViewById<TextView>(R.id.tv_item_qing_ping)
                val tvLuoma = v.findViewById<TextView>(R.id.tv_item_ruoma)
                tvKana.text  = if (isHiragana) cell.kana!!.ping else cell.kana!!.pian
                tvLuoma.text = cell.kana.luoma
                v
            }
            else -> {
                // TYPE_EMPTY
                convertView ?: inflater.inflate(R.layout.item_grid_view_empty, parent, false)
            }
        }
    }

    // -----------------------------------------------------------------------
    // 外部调用
    // -----------------------------------------------------------------------

    fun setMode(hiragana: Boolean) {
        isHiragana = hiragana
        notifyDataSetChanged()
    }

    fun updateData(newList: List<Kana>) {
        cells = buildCells(newList)
        notifyDataSetChanged()
    }

    fun getCellAt(position: Int): KanaCell? = cells.getOrNull(position)

    /** 点行标签时，获取该行所有有效音节的罗马字 */
    fun getRowSounds(rowLabelPosition: Int): List<String> {
        return (1..5).mapNotNull { offset ->
            cells.getOrNull(rowLabelPosition + offset)?.kana?.luoma
        }.filter { it.isNotEmpty() }
    }

    /** 点列标签时，获取该列所有有效音节的罗马字 */
    fun getColSounds(colLabelPosition: Int): List<String> {
        val numCols = if (kanaType == "ao") 6 else 6
        val col = colLabelPosition % numCols
        val totalRows = cells.size / numCols
        return (1 until totalRows).mapNotNull { row ->
            cells.getOrNull(row * numCols + col)?.kana?.luoma
        }.filter { it.isNotEmpty() }
    }
}