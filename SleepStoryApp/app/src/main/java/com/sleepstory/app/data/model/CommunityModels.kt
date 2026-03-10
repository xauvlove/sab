package com.sleepstory.app.data.model

/**
 * 他人创作（社区故事）数据模型
 */

/**
 * 社区故事列表项模型
 */
data class CommunityStoryModel(
    val id: Long,
    val title: String,
    val summary: String?,
    val category: String?,
    val tags: String?,
    val likesCount: Int,
    val playsCount: Int,
    val userNickname: String,
    val userAvatar: String?,
    val createdAt: String?,
    val isLiked: Boolean = false
)

/**
 * 社区故事详情模型
 */
data class CommunityStoryDetailModel(
    val id: Long,
    val title: String,
    val summary: String?,
    val content: String,
    val voiceConfig: String?,  // JSON格式：{"voiceId": "xxx", "speed": 1.0, "pitch": 1.0}
    val category: String?,
    val tags: String?,
    val likesCount: Int,
    val playsCount: Int,
    val userId: String,
    val userNickname: String,
    val userAvatar: String?,
    val createdAt: String?,
    val isOwner: Boolean = false,
    val isLiked: Boolean = false
)

/**
 * 发布故事请求模型
 */
data class PublishStoryRequest(
    val title: String,
    val summary: String?,
    val content: String,
    val voiceConfig: String?,  // JSON格式
    val category: String?,
    val tags: String?
)

/**
 * 语音配置模型
 */
data class VoiceConfig(
    val voiceId: String = "xiaoxiao",
    val speed: Float = 1.0f,
    val pitch: Float = 1.0f
) {
    fun toJson(): String {
        return """{"voiceId":"$voiceId","speed":$speed,"pitch":$pitch}"""
    }

    companion object {
        fun fromJson(json: String?): VoiceConfig {
            if (json.isNullOrBlank()) {
                return VoiceConfig()
            }
            return try {
                // 简单解析JSON
                val voiceId = Regex("""\"voiceId"\s*:\s*"([^"]+)"""").find(json)?.groupValues?.get(1) ?: "xiaoxiao"
                val speed = Regex("""\"speed"\s*:\s*([\d.]+)""").find(json)?.groupValues?.get(1)?.toFloatOrNull() ?: 1.0f
                val pitch = Regex("""\"pitch"\s*:\s*([\d.]+)""").find(json)?.groupValues?.get(1)?.toFloatOrNull() ?: 1.0f
                VoiceConfig(voiceId, speed, pitch)
            } catch (e: Exception) {
                VoiceConfig()
            }
        }
    }
}

/**
 * TTS请求模型
 */
data class TTSRequest(
    val text: String,
    val voiceId: String = "xiaoxiao",
    val speed: Float = 1.0f,
    val pitch: Float = 1.0f
)

/**
 * TTS响应模型
 */
data class TTSResult(
    val audioUrl: String,
    val duration: Int
)
