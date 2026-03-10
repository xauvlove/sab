package com.sleepstory.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore扩展
private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "token_prefs")

/**
 * Token数据仓库
 * 专门管理Token的获取和存储
 */
@Singleton
class TokenRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
    }

    /**
     * 获取Token（同步方式）
     */
    suspend fun getToken(): String? {
        return context.tokenDataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }.first()
    }

    /**
     * 保存Token
     */
    suspend fun saveToken(token: String) {
        context.tokenDataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    /**
     * 清除Token
     */
    suspend fun clearToken() {
        context.tokenDataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }

    /**
     * 检查是否已登录
     */
    suspend fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}
