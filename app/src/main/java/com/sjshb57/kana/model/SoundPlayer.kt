package com.sjshb57.kana.model

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {

    private var mediaPlayer: MediaPlayer? = null

    private var playList: List<String> = emptyList()
    private var currentIndex: Int = -1

    private var appContext: Context? = null

    /** 播放单个音节（点击普通格子） */
    fun play(context: Context, luoma: String) {
        stopSequence()
        playSingle(context.applicationContext, luoma, null)
    }

    /** 顺序播放一组音节（点击行/列标签） */
    fun playSequence(context: Context, luomaList: List<String>) {
        stopSequence()
        appContext = context.applicationContext
        playList = luomaList.filter { it.isNotEmpty() }
        currentIndex = 0
        playNext()
    }

    private fun playNext() {
        val ctx = appContext ?: return
        if (currentIndex < 0 || currentIndex >= playList.size) {
            // 播完，清空引用
            appContext = null
            currentIndex = -1
            return
        }
        playSingle(ctx, playList[currentIndex]) {
            currentIndex++
            playNext()
        }
    }

    private fun playSingle(appCtx: Context, luoma: String, onComplete: (() -> Unit)?) {
        stop()
        try {
            val afd = appCtx.assets.openFd("$luoma.mp3")
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                setOnPreparedListener { it.start() }
                setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                    onComplete?.invoke()
                }
                setOnErrorListener { mp, _, _ ->
                    mp.release()
                    mediaPlayer = null
                    onComplete?.invoke()
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mediaPlayer = null
            onComplete?.invoke()
        }
    }

    fun stop() {
        mediaPlayer?.let {
            runCatching { if (it.isPlaying) it.stop() }
            it.release()
        }
        mediaPlayer = null
    }

    fun stopSequence() {
        playList = emptyList()
        currentIndex = -1
        appContext = null
        stop()
    }

    fun release() = stopSequence()
}