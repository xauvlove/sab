package com.sleepstory.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleepstory.app.data.api.StoryApiService
import com.sleepstory.app.data.model.DailyStatsModel
import com.sleepstory.app.data.model.UserPreferenceModel
import com.sleepstory.app.data.model.UserStatsModel
import com.sleepstory.app.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 用户资料ViewModel
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storyApiService: StoryApiService,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    /**
     * 加载用户数据（统计信息和偏好设置）
     */
    fun loadUserData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepository.getToken()
                if (token.isNullOrEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = false
                    )
                    return@launch
                }

                // Load user stats
                val statsResponse = storyApiService.getUserStats(token)
                if (statsResponse.isSuccessful && statsResponse.body()?.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        userStats = statsResponse.body()?.data
                    )
                }

                // Load user preferences
                val prefsResponse = storyApiService.getUserPreferences(token)
                if (prefsResponse.isSuccessful && prefsResponse.body()?.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        userPreferences = prefsResponse.body()?.data,
                        isLoading = false,
                        isLoggedIn = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * 更新用户偏好设置
     */
    fun updatePreferences(preferences: UserPreferenceModel) {
        viewModelScope.launch {
            try {
                val token = tokenRepository.getToken() ?: return@launch
                val response = storyApiService.updateUserPreferences(preferences, token)
                if (response.isSuccessful && response.body()?.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        userPreferences = response.body()?.data
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * 退出登录
     */
    fun logout() {
        tokenRepository.clearToken()
        _uiState.value = ProfileUiState()
    }

    /**
     * 刷新用户数据
     */
    fun refresh() {
        loadUserData()
    }
}

/**
 * 用户资料UI状态
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val userStats: UserStatsModel? = null,
    val userPreferences: UserPreferenceModel? = null
)
