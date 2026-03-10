package com.sleepstory.app.data.api

import com.sleepstory.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 他人创作（社区故事）API服务接口
 */
interface CommunityApiService {

    /**
     * 获取故事列表（Feed流）
     */
    @GET("api/stories/feed")
    suspend fun getStoryFeed(
        @Query("category") category: String? = null,
        @Query("orderBy") orderBy: String? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<List<CommunityStoryModel>>>

    /**
     * 获取故事详情
     */
    @GET("api/stories/{storyId}")
    suspend fun getStoryDetail(
        @Path("storyId") storyId: Long,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<CommunityStoryDetailModel>>

    /**
     * 切换点赞状态
     */
    @POST("api/stories/{storyId}/like")
    suspend fun toggleLike(
        @Path("storyId") storyId: Long,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Map<String, Boolean>>>

    /**
     * 获取用户发布的故事列表
     */
    @GET("api/stories/user/{userId}")
    suspend fun getUserStories(
        @Path("userId") userId: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): Response<ApiResponse<List<CommunityStoryModel>>>

    /**
     * 发布故事
     */
    @POST("api/stories/publish")
    suspend fun publishStory(
        @Body request: PublishStoryRequest,
        @Header("Authorization") token: String
    ): Response<ApiResponse<CommunityStoryDetailModel>>

    /**
     * 删除故事
     */
    @DELETE("api/stories/{storyId}")
    suspend fun deleteStory(
        @Path("storyId") storyId: Long,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>
}
