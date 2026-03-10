package com.sleepstory.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleepstory.app.data.api.UserApiService
import com.sleepstory.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 验证码登录页面状态
 */
data class SmsLoginUiState(
    val phone: String = "",
    val code: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val countdownSeconds: Int = 60,
    val isCountingDown: Boolean = false,
    val codeSent: Boolean = false
)

/**
 * 验证码登录ViewModel
 */
@HiltViewModel
class SmsLoginViewModel @Inject constructor(
    private val userApiService: UserApiService,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SmsLoginUiState())
    val uiState: StateFlow<SmsLoginUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    fun onPhoneChange(phone: String) {
        _uiState.update { it.copy(phone = phone, errorMessage = null) }
    }

    fun onCodeChange(code: String) {
        _uiState.update { it.copy(code = code, errorMessage = null) }
    }

    fun sendCode() {
        val currentState = _uiState.value

        // 验证手机号
        if (currentState.phone.length != 11) {
            _uiState.update { it.copy(errorMessage = "请输入正确的手机号") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = userApiService.sendVerificationCode(currentState.phone)
                if (response.isSuccessful && response.body()?.code == 200) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            codeSent = true,
                            isCountingDown = true,
                            countdownSeconds = 60
                        )
                    }
                    // 开始倒计时
                    startCountdown()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = response.body()?.message ?: "发送失败，请重试"
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

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            var seconds = 60
            while (seconds > 0) {
                delay(1000)
                seconds--
                _uiState.update { it.copy(countdownSeconds = seconds) }
            }
            _uiState.update { it.copy(isCountingDown = false) }
        }
    }

    fun login() {
        val currentState = _uiState.value

        // 验证输入
        when {
            currentState.phone.length != 11 -> {
                _uiState.update { it.copy(errorMessage = "请输入正确的手机号") }
                return
            }
            currentState.code.length != 6 -> {
                _uiState.update { it.copy(errorMessage = "请输入6位验证码") }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = userApiService.smsLogin(
                    phone = currentState.phone,
                    code = currentState.code
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
                                errorMessage = response.body()?.message ?: "登录失败"
                            )
                        }
                    }
                } else {
                    val errorMsg = response.body()?.message ?: "登录失败"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMsg
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

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}
