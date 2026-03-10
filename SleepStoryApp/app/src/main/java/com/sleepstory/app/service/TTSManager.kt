package com.sleepstory.app.service

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TTS（文字转语音）管理器
 * 用于将社区故事的文字内容转换为语音播放
 */
@Singleton
class TTSManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "TTSManager"
    }

    // TTS状态
    private var textToSpeech: TextToSpeech? = null
    private var mediaPlayer: MediaPlayer? = null

    // 状态流
    private val _ttsState = MutableStateFlow(TTSState.IDLE)
    val ttsState: StateFlow<TTSState> = _ttsState.asStateFlow()

    // 当前播放进度
    private val _playbackProgress = MutableStateFlow(0f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    // 当前播放的文字
    private var currentText: String = ""

    // 语音配置
    private var currentVoiceId: String = "zh-CN"
    private var currentSpeed: Float = 1.0f
    private var currentPitch: Float = 1.0f

    // 回调
    private var onCompletionListener: (() -> Unit)? = null
    private var onErrorListener: ((String) -> Unit)? = null

    // 可用的语音列表
    private val availableVoices = mutableListOf<Voice>()

    /**
     * 初始化TTS引擎
     */
    fun initialize(onReady: () -> Unit = {}) {
        if (textToSpeech != null) {
            onReady()
            return
        }

        _ttsState.value = TTSState.INITIALIZING

        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // 设置中文语言
                val result = textToSpeech?.setLanguage(Locale.CHINESE)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w(TAG, "Chinese language not supported, using default")
                }

                // 获取可用的语音
                textToSpeech?.voices?.let { voices ->
                    availableVoices.clear()
                    availableVoices.addAll(voices.filter { it.locale.language == "zh" })
                }

                // 设置音频流
                textToSpeech?.setSpeechRate(currentSpeed)
                textToSpeech?.setPitch(currentPitch)

                // 设置进度监听
                textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _ttsState.value = TTSState.PLAYING
                    }

                    override fun onDone(utteranceId: String?) {
                        _ttsState.value = TTSState.IDLE
                        _playbackProgress.value = 1f
                        onCompletionListener?.invoke()
                    }

                    override fun onError(utteranceId: String?) {
                        _ttsState.value = TTSState.ERROR
                        onErrorListener?.invoke("语音播放出错")
                    }
                })

                _ttsState.value = TTSState.READY
                onReady()
                Log.d(TAG, "TTS initialized successfully")
            } else {
                _ttsState.value = TTSState.ERROR
                Log.e(TAG, "TTS initialization failed: $status")
                onErrorListener?.invoke("语音引擎初始化失败")
            }
        }
    }

    /**
     * 设置语音配置
     */
    fun setVoiceConfig(voiceId: String, speed: Float = 1.0f, pitch: Float = 1.0f) {
        currentVoiceId = voiceId
        currentSpeed = speed
        currentPitch = pitch

        textToSpeech?.setSpeechRate(speed)
        textToSpeech?.setPitch(pitch)

        // 尝试设置特定语音
        if (voiceId.isNotBlank()) {
            val voice = availableVoices.find {
                it.name.contains(voiceId, ignoreCase = true) ||
                it.name.contains("zh-CN", ignoreCase = true)
            }
            voice?.let {
                textToSpeech?.voice = it
            }
        }
    }

    /**
     * 播放文字
     */
    fun speak(text: String, onComplete: () -> Unit = {}, onError: (String) -> Unit = {}) {
        if (text.isBlank()) {
            onError("播放内容为空")
            return
        }

        // 确保TTS已初始化
        if (textToSpeech == null) {
            initialize {
                speak(text, onComplete, onError)
            }
            return
        }

        currentText = text
        onCompletionListener = onComplete
        onErrorListener = onError
        _playbackProgress.value = 0f

        // 停止当前播放
        stop()

        // 使用UUID作为utteranceId
        val utteranceId = UUID.randomUUID().toString()

        _ttsState.value = TTSState.PLAYING

        // 播放
        textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            utteranceId
        )
    }

    /**
     * 暂停播放
     */
    fun pause() {
        if (_ttsState.value == TTSState.PLAYING) {
            // TTS本身不支持暂停，我们通过停止来模拟
            textToSpeech?.stop()
            _ttsState.value = TTSState.PAUSED
        }
    }

    /**
     * 恢复播放
     */
    fun resume() {
        if (_ttsState.value == TTSState.PAUSED && currentText.isNotBlank()) {
            speak(currentText)
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        textToSpeech?.stop()
        _ttsState.value = TTSState.IDLE
        _playbackProgress.value = 0f
    }

    /**
     * 释放资源
     */
    fun release() {
        stop()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null

        mediaPlayer?.release()
        mediaPlayer = null

        _ttsState.value = TTSState.IDLE
        availableVoices.clear()
    }

    /**
     * 检查是否正在播放
     */
    fun isPlaying(): Boolean {
        return _ttsState.value == TTSState.PLAYING
    }

    /**
     * 获取可用的语音列表
     */
    fun getAvailableVoices(): List<String> {
        return availableVoices.map { it.name }
    }
}

/**
 * TTS状态枚举
 */
enum class TTSState {
    IDLE,           // 空闲
    INITIALIZING,   // 初始化中
    READY,         // 就绪
    PLAYING,       // 播放中
    PAUSED,        // 暂停
    ERROR          // 错误
}
