package com.sleepstory.app.data.api

import com.sleepstory.app.data.model.ApiResponse
import com.sleepstory.app.data.model.AuthResponse
import com.sleepstory.app.data.model.LoginRequest
import com.sleepstory.app.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 用户API服务接口
 */
interface UserApiService {

    /**
     * 用户注册
     */
    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<AuthResponse>>

    /**
     * 用户登录
     */
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<AuthResponse>>

    /**
     * 获取当前用户信息
     */
    @GET("api/auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): Response<ApiResponse<AuthResponse.UserInfo>>

    /**
     * 刷新Token
     */
    @POST("api/auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<ApiResponse<AuthResponse>>

    /**
     * 检查手机号是否已注册
     */
    @GET("api/auth/check-phone")
    suspend fun checkPhone(
        @Query("phone") phone: String
    ): Response<ApiResponse<Boolean>>

    // 扩展方法，方便调用
    suspend fun register(
        phone: String,
        password: String,
        nickname: String? = null
    ): Response<ApiResponse<AuthResponse>> {
        return register(RegisterRequest(phone, password, nickname))
    }

    suspend fun login(
        phone: String,
        password: String
    ): Response<ApiResponse<AuthResponse>> {
        return login(LoginRequest(phone, password))
    }
}
