package com.sleepstory.app.di

import com.sleepstory.app.data.repository.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 认证拦截器
 * 自动为需要认证的请求添加JWT token
 */
class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // 检查请求是否需要认证（根据路径判断）
        val needsAuth = needsAuthentication(originalRequest.url.encodedPath)
        
        if (needsAuth) {
            val token = tokenRepository.getToken()
            if (token != null) {
                val authenticatedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                return chain.proceed(authenticatedRequest)
            }
        }
        
        return chain.proceed(originalRequest)
    }

    /**
     * 判断指定路径是否需要认证
     */
    private fun needsAuthentication(path: String): Boolean {
        // 需要认证的API路径
        val authRequiredPaths = listOf(
            "/api/user/",
            "/api/favorites/",
            "/api/stories/generate",
            "/api/stories/tts",
            "/api/user/play-history"
        )
        
        return authRequiredPaths.any { path.contains(it) }
    }
}