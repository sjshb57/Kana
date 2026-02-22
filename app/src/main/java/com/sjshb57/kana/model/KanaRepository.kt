package com.sjshb57.kana.model

import android.content.Context
import com.google.gson.Gson

object KanaRepository {

    // 缓存，避免重复读文件
    private var cache: KanaData? = null

    /**
     * 读取 assets/kana.json，返回完整数据
     * 第一次调用会读文件并缓存，之后直接返回缓存
     */
    fun load(context: Context): KanaData {
        if (cache != null) return cache!!
        val json = context.applicationContext
            .assets
            .open("kana.json")
            .bufferedReader()
            .use { it.readText() }
        cache = Gson().fromJson(json, KanaData::class.java)
        return cache!!
    }

    /**
     * 根据分类名获取对应列表
     * @param type "qing" | "zhuo" | "ao"
     */
    fun getByType(context: Context, type: String): List<Kana> {
        val data = load(context)
        return when (type) {
            "qing" -> data.qing
            "zhuo" -> data.zhuo
            "ao"   -> data.ao
            else   -> emptyList()
        }
    }

    /**
     * 获取所有假名合并列表（用于滑动练习随机出题）
     */
    fun getAll(context: Context): List<Kana> {
        val data = load(context)
        return data.qing + data.zhuo + data.ao
    }

    /**
     * 清除缓存（一般用不到，保留备用）
     */
    fun clearCache() {
        cache = null
    }
}