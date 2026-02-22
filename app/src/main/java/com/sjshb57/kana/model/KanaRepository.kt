package com.sjshb57.kana.model

import android.content.Context
import org.json.JSONObject

object KanaRepository {

    private var qingCache: List<Kana>? = null
    private var zhuoCache: List<Kana>? = null
    private var aoCache:   List<Kana>? = null

    fun getByType(context: Context, type: String): List<Kana> {
        return when (type) {
            "qing" -> qingCache ?: load(context, "qing").also { qingCache = it }
            "zhuo" -> zhuoCache ?: load(context, "zhuo").also { zhuoCache = it }
            "ao"   -> aoCache   ?: load(context, "ao").also   { aoCache   = it }
            else   -> emptyList()
        }
    }

    private fun load(context: Context, type: String): List<Kana> {
        val json = context.assets.open("kana.json")
            .bufferedReader()
            .use { it.readText() }

        val root  = JSONObject(json)
        val array = root.getJSONArray(type)
        val list  = mutableListOf<Kana>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                Kana(
                    ping  = obj.optString("ping",  ""),
                    pian  = obj.optString("pian",  ""),
                    luoma = obj.optString("luoma", "")
                )
            )
        }
        return list
    }
}