package com.sleepstory.app.ui.screens.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleepstory.app.data.api.StoryApiService
import com.sleepstory.app.data.model.StoryDetailModel
import com.sleepstory.app.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 故事详情ViewModel
 */
@HiltViewModel
class StoryDetailViewModel @Inject constructor(
    private val storyApiService: StoryApiService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoryDetailUiState())
    val uiState: StateFlow<StoryDetailUiState> = _uiState.asStateFlow()

    /**
     * 加载故事详情
     */
    fun loadStoryDetail(storyId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepository.getToken()
                val response = storyApiService.getStoryDetail(storyId, token)
                if (response.isSuccessful && response.body()?.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        storyDetail = response.body()?.data
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = response.body()?.message ?: "获取故事详情失败"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "网络错误"
                )
            }
        }
    }

    /**
     * 切换收藏状态
     */
    fun toggleFavorite(storyId: String) {
        viewModelScope.launch {
            try {
                val token = tokenRepository.getToken() ?: return@launch
                val response = storyApiService.toggleFavorite(
                    mapOf("storyId" to storyId),
                    token
                )
                if (response.isSuccessful && response.body()?.code == 200) {
                    loadStoryDetail(storyId)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}

/**
 * 故事详情UI状态
 */
data class StoryDetailUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val storyDetail: StoryDetailModel? = null
)
