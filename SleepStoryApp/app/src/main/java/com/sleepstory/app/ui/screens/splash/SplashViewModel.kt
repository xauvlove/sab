package com.sleepstory.app.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sleepstory.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * 启动页ViewModel
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    /**
     * 是否已登录
     */
    val isLoggedIn: StateFlow<Boolean> = userRepository.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    /**
     * 是否已完成引导评估
     * TODO: 从本地存储读取
     */
    val onboardingCompleted: StateFlow<Boolean> = kotlinx.coroutines.flow.MutableStateFlow(false)
}
