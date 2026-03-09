package com.sleepstory.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleepstory.app.data.api.UserApiService
import com.sleepstory.app.data.model.AuthResponse
import com.sleepstory.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 注册页面状态
 */
data class RegisterUiState(
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nickname: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

/**
 * 注册ViewModel
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userApiService: UserApiService,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onPhoneChange(phone: String) {
        _uiState.update { it.copy(phone = phone, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errorMessage = null) }
    }

    fun onNicknameChange(nickname: String) {
        _uiState.update { it.copy(nickname = nickname) }
    }

    fun register() {
        val currentState = _uiState.value

        // 验证输入
        when {
            currentState.phone.length != 11 -> {
                _uiState.update { it.copy(errorMessage = "请输入正确的手机号") }
                return
            }
            currentState.password.length < 6 -> {
                _uiState.update { it.copy(errorMessage = "密码长度不能少于6位") }
                return
            }
            currentState.password != currentState.confirmPassword -> {
                _uiState.update { it.copy(errorMessage = "两次输入的密码不一致") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = userApiService.register(
                    phone = currentState.phone,
                    password = currentState.password,
                    nickname = currentState.nickname.takeIf { it.isNotBlank() }
                )

                if (response.isSuccessful && response.body() != null) {
                    val authData = response.body()!!.data
                    if (authData != null) {
                        // 保存用户数据
                        userRepository.saveAuthData(authData)
                        _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.body()?.message ?: "注册失败"
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "注册失败: ${response.message()}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "网络错误，请稍后重试"
                    )
                }
            }
        }
    }
}
