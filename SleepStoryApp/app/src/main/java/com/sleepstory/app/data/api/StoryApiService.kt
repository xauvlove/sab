package com.sleepstory.app.data.api

import com.sleepstory.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 故事API服务接口
 */
interface StoryApiService {

    /**
     * 生成故事
     */
    @POST("api/stories/generate")
    suspend fun generateStory(
        @Body request: com.sleepstory.app.data.model.StoryGenerationRequest
    ): Response<com.sleepstory.app.data.model.StoryContent>

    /**
     * 生成语音
     */
    @POST("api/stories/tts")
    suspend fun generateTTS(
        @Body text: String
    ): Response<TTSResponse>

    // ==================== 发现页API ====================

    /**
     * 获取发现页数据
     */
    @GET("api/discovery")
    suspend fun getDiscoveryData(
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<DiscoveryResponse>>

    /**
     * 搜索故事
     */
    @GET("api/discovery/search")
    suspend fun searchStories(
        @Query("keyword") keyword: String
    ): Response<ApiResponse<List<StoryModel>>>

    /**
     * 获取分类故事
     */
    @GET("api/discovery/category/{category}")
    suspend fun getStoriesByCategory(
        @Path("category") category: String
    ): Response<ApiResponse<List<StoryModel>>>

    /**
     * 获取故事详情
     */
    @GET("api/discovery/story/{storyId}")
    suspend fun getStoryDetail(
        @Path("storyId") storyId: String,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<StoryDetailModel>>

    // ==================== 收藏API ====================

    /**
     * 切换收藏状态
     */
    @POST("api/favorites/toggle")
    suspend fun toggleFavorite(
        @Body request: Map<String, String>,
        @Header("Authorization") token: String
    ): Response<ApiResponse<FavoriteToggleResponse>>

    /**
     * 获取收藏列表
     */
    @GET("api/favorites/list")
    suspend fun getFavorites(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<StoryModel>>>

    /**
     * 检查是否已收藏
     */
    @GET("api/favorites/check/{storyId}")
    suspend fun checkFavorite(
        @Path("storyId") storyId: String,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<Map<String, Boolean>>>

    // ==================== 用户API ====================

    /**
     * 获取用户统计
     */
    @GET("api/user/stats")
    suspend fun getUserStats(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserStatsModel>>

    /**
     * 获取用户偏好设置
     */
    @GET("api/user/preferences")
    suspend fun getUserPreferences(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserPreferenceModel>>

    /**
     * 更新用户偏好设置
     */
    @PUT("api/user/preferences")
    suspend fun updateUserPreferences(
        @Body request: UserPreferenceModel,
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserPreferenceModel>>

    /**
     * 记录播放历史
     */
    @POST("api/user/play-history")
    suspend fun recordPlayHistory(
        @Body request: Map<String, Any>,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>
}

data class TTSResponse(
    val audioUrl: String,
    val duration: Int
)
