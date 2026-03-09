package com.sleepstory.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sleepstory.app.data.model.AuthResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore扩展
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

/**
 * 用户数据仓库
 * 管理用户认证数据的本地存储
 */
@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_PHONE_KEY = stringPreferencesKey("user_phone")
        private val USER_NICKNAME_KEY = stringPreferencesKey("user_nickname")
        private val USER_AVATAR_KEY = stringPreferencesKey("user_avatar")
    }

    /**
     * 保存认证数据
     */
    suspend fun saveAuthData(authData: AuthResponse) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = authData.token
            prefs[USER_ID_KEY] = authData.user.id
            prefs[USER_PHONE_KEY] = authData.user.phone
            prefs[USER_NICKNAME_KEY] = authData.user.nickname
            authData.user.avatarUrl?.let { prefs[USER_AVATAR_KEY] = it }
        }
    }

    /**
     * 获取Token
     */
    val token: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]
    }

    /**
     * 获取用户ID
     */
    val userId: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_ID_KEY]
    }

    /**
     * 获取用户手机号
     */
    val userPhone: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_PHONE_KEY]
    }

    /**
     * 获取用户昵称
     */
    val userNickname: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_NICKNAME_KEY]
    }

    /**
     * 获取用户头像
     */
    val userAvatar: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_AVATAR_KEY]
    }

    /**
     * 检查是否已登录
     */
    val isLoggedIn: Flow<Boolean> = token.map { it != null }

    /**
     * 清除用户数据（退出登录）
     */
    suspend fun clearUserData() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    /**
     * 获取Authorization Header
     */
    suspend fun getAuthHeader(): String? {
        return token.map { it?.let { "Bearer $it" } }.map { it }.toString()
    }
}
