package com.sleepstory.app.data.model

/**
 * 认证响应数据
 */
data class AuthResponse(
    val token: String,
    val tokenType: String,
    val expiresIn: Long,
    val user: UserInfo
) {
    data class UserInfo(
        val id: String,
        val phone: String,
        val nickname: String,
        val avatarUrl: String?,
        val createdAt: String
    )
}

/**
 * 注册请求
 */
data class RegisterRequest(
    val phone: String,
    val password: String,
    val nickname: String? = null
)

/**
 * 登录请求
 */
data class LoginRequest(
    val phone: String,
    val password: String
)

/**
 * API通用响应包装
 */
data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)
