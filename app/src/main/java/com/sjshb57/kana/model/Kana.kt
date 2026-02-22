package com.sjshb57.kana.model

/**
 * 单个假名条目
 * @param ping  平假名，例如：あ
 * @param pian  片假名，例如：ア
 * @param luoma 罗马字，例如：a
 */
data class Kana(
    val ping: String,
    val pian: String,
    val luoma: String
)

/**
 * kana.json 根对象
 * @param qing 清音（51条，含五十音本体）
 * @param zhuo 浊音 + 半浊音（25条）
 * @param ao   拗音（33条）
 */
data class KanaData(
    val qing: List<Kana>,
    val zhuo: List<Kana>,
    val ao: List<Kana>
)