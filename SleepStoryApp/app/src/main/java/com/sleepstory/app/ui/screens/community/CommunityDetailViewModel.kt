package com.sleepstory.app.ui.screens.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleepstory.app.data.model.*
import com.sleepstory.app.data.repository.CommunityRepository
import com.sleepstory.app.service.TTSManager
import com.sleepstory.app.service.TTSState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 他人创作故事详情ViewModel
 */
@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val ttsManager: TTSManager
) : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState: StateFlow<CommunityDetailUiState> = _uiState.asStateFlow()

    // TTS状态
    val ttsState: StateFlow<TTSState> = ttsManager.ttsState
    val ttsProgress: StateFlow<Float> = ttsManager.playbackProgress

    // 当前故事ID
    private var currentStoryId: Long = 0

    // 初始化TTS
    init {
        ttsManager.initialize()
    }

    /**
     * 加载故事详情
     */
    fun loadStoryDetail(storyId: Long) {
        currentStoryId = storyId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = communityRepository.getStoryDetail(storyId)

            result.onSuccess { story ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        story = story
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
        }
    }

    /**
     * 播放TTS
     */
    fun playTTS() {
        val story = _uiState.value.story ?: return

        // 解析语音配置
        val voiceConfig = VoiceConfig.fromJson(story.voiceConfig)

        // 设置语音配置
        ttsManager.setVoiceConfig(
            voiceId = voiceConfig.voiceId,
            speed = voiceConfig.speed,
            pitch = voiceConfig.pitch
        )

        // 播放
        ttsManager.speak(
            text = story.content,
            onComplete = {
                // 播放完成
            },
            onError = { error ->
                _uiState.update { it.copy(error = error) }
            }
        )
    }

    /**
     * 暂停TTS
     */
    fun pauseTTS() {
        ttsManager.pause()
    }

    /**
     * 停止TTS
     */
    fun stopTTS() {
        ttsManager.stop()
    }

    /**
     * 切换点赞状态
     */
    fun toggleLike() {
        val storyId = currentStoryId

        viewModelScope.launch {
            val result = communityRepository.toggleLike(storyId)

            result.onSuccess { isLiked ->
                _uiState.update { state ->
                    state.copy(
                        story = state.story?.copy(
                            isLiked = isLiked,
                            likesCount = if (isLiked) {
                                state.story.likesCount + 1
                            } else {
                                state.story.likesCount - 1
                            }
                        )
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }

    /**
     * 删除故事
     */
    fun deleteStory() {
        val storyId = currentStoryId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = communityRepository.deleteStory(storyId)

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isDeleted = true
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
        }
    }

    /**
     * 清除错误
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.release()
    }
}

/**
 * 社区故事详情UI状态
 */
data class CommunityDetailUiState(
    val isLoading: Boolean = false,
    val story: CommunityStoryDetailModel? = null,
    val error: String? = null,
    val isDeleted: Boolean = false
)
