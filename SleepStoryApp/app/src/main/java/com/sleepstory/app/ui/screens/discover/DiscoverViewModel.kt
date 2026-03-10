package com.sleepstory.app.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleepstory.app.data.api.StoryApiService
import com.sleepstory.app.data.model.CategoryModel
import com.sleepstory.app.data.model.DiscoveryResponse
import com.sleepstory.app.data.model.StoryModel
import com.sleepstory.app.data.model.TagModel
import com.sleepstory.app.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 发现页ViewModel
 */
@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val storyApiService: StoryApiService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        loadDiscoveryData()
    }

    /**
     * 加载发现页数据（热门标签、分类、排行榜）
     */
    fun loadDiscoveryData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepository.getToken()
                val response = storyApiService.getDiscoveryData(token)
                if (response.isSuccessful && response.body()?.code == 200) {
                    val data = response.body()?.data
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        hotTags = data?.hotTags ?: emptyList(),
                        categories = data?.categories ?: emptyList(),
                        rankings = data?.rankings ?: emptyList()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = response.body()?.message ?: "获取数据失败"
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
     * 刷新数据
     */
    fun refresh() {
        loadDiscoveryData()
    }
}

/**
 * 发现页UI状态
 */
data class DiscoverUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val hotTags: List<TagModel> = emptyList(),
    val categories: List<CategoryModel> = emptyList(),
    val rankings: List<StoryModel> = emptyList()
)
