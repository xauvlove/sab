package com.sleepstory.app.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleepstory.app.data.api.StoryApiService
import com.sleepstory.app.data.model.StoryModel
import com.sleepstory.app.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 收藏ViewModel
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val storyApiService: StoryApiService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepository.getToken()
                if (token.isNullOrEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "请先登录"
                    )
                    return@launch
                }

                val response = storyApiService.getFavorites(token)
                if (response.isSuccessful && response.body()?.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        favorites = response.body()?.data ?: emptyList()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = response.body()?.message ?: "获取收藏失败"
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

    fun toggleFavorite(storyId: String) {
        viewModelScope.launch {
            try {
                val token = tokenRepository.getToken() ?: return@launch
                val response = storyApiService.toggleFavorite(
                    mapOf("storyId" to storyId),
                    token
                )
                if (response.isSuccessful && response.body()?.code == 200) {
                    loadFavorites()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun refresh() {
        loadFavorites()
    }
}

/**
 * 收藏页UI状态
 */
data class FavoritesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val favorites: List<StoryModel> = emptyList()
)
