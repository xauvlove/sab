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
 * 他人创作（社区故事）ViewModel
 */
@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val ttsManager: TTSManager
) : ViewModel() {

    // UI状态
    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    // TTS状态
    val ttsState: StateFlow<TTSState> = ttsManager.ttsState
    val ttsProgress: StateFlow<Float> = ttsManager.playbackProgress

    // 当前选中的分类
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // 当前选中的排序方式
    private val _selectedOrder = MutableStateFlow("latest")
    val selectedOrder: StateFlow<String> = _selectedOrder.asStateFlow()

    init {
        // 初始化TTS
        ttsManager.initialize()
    }

    /**
     * 加载故事列表
     */
    fun loadStories(refresh: Boolean = false) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            if (refresh) {
                _uiState.update { it.copy(stories = emptyList()) }
            }

            val result = communityRepository.getStoryFeed(
                category = _selectedCategory.value,
                orderBy = _selectedOrder.value,
                page = 1,
                size = 20
            )

            result.onSuccess { stories ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        stories = stories,
                        isRefreshing = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message,
                        isRefreshing = false
                    )
                }
            }
        }
    }

    /**
     * 加载更多故事
     */
    fun loadMoreStories() {
        if (_uiState.value.isLoading || _uiState.value.isLoadMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadMore = true) }

            val result = communityRepository.getStoryFeed(
                category = _selectedCategory.value,
                orderBy = _selectedOrder.value,
                page = (_uiState.value.stories.size / 20) + 1,
                size = 20
            )

            result.onSuccess { stories ->
                _uiState.update {
                    it.copy(
                        isLoadMore = false,
                        stories = it.stories + stories
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoadMore = false,
                        error = error.message
                    )
                }
            }
        }
    }

    /**
     * 切换分类
     */
    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        loadStories(refresh = true)
    }

    /**
     * 切换排序方式
     */
    fun selectOrder(order: String) {
        _selectedOrder.value = order
        loadStories(refresh = true)
    }

    /**
     * 刷新列表
     */
    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadStories(refresh = true)
    }

    /**
     * 切换点赞状态
     */
    fun toggleLike(storyId: Long) {
        viewModelScope.launch {
            val result = communityRepository.toggleLike(storyId)
            result.onSuccess { isLiked ->
                // 更新列表中的点赞状态
                _uiState.update { state ->
                    state.copy(
                        stories = state.stories.map { story ->
                            if (story.id == storyId) {
                                story.copy(
                                    isLiked = isLiked,
                                    likesCount = if (isLiked) story.likesCount + 1 else story.likesCount - 1
                                )
                            } else {
                                story
                            }
                        }
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }

    /**
     * 播放TTS
     */
    fun playTTS(text: String, voiceConfig: VoiceConfig? = null) {
        voiceConfig?.let {
            ttsManager.setVoiceConfig(it.voiceId, it.speed, it.pitch)
        }
        ttsManager.speak(
            text = text,
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
 * 社区故事UI状态
 */
data class CommunityUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadMore: Boolean = false,
    val stories: List<CommunityStoryModel> = emptyList(),
    val error: String? = null
)
