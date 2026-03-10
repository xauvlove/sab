package com.sleepstory.app.data.repository

import com.sleepstory.app.data.api.CommunityApiService
import com.sleepstory.app.data.model.*
import com.sleepstory.app.data.repository.TokenRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 他人创作（社区故事）仓库
 */
@Singleton
class CommunityRepository @Inject constructor(
    private val communityApiService: CommunityApiService,
    private val tokenRepository: TokenRepository
) {

    /**
     * 获取故事列表（Feed流）
     */
    suspend fun getStoryFeed(
        category: String? = null,
        orderBy: String? = null,
        page: Int = 1,
        size: Int = 20
    ): Result<List<CommunityStoryModel>> {
        return try {
            val token = tokenRepository.getToken().first()
            val response = communityApiService.getStoryFeed(
                category = category,
                orderBy = orderBy,
                page = page,
                size = size,
                token = token
            )
            if (response.isSuccessful && response.body()?.code == 200) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "获取故事列表失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 获取故事详情
     */
    suspend fun getStoryDetail(storyId: Long): Result<CommunityStoryDetailModel> {
        return try {
            val token = tokenRepository.getToken().first()
            val response = communityApiService.getStoryDetail(
                storyId = storyId,
                token = token
            )
            if (response.isSuccessful && response.body()?.code == 200) {
                response.body()?.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("故事不存在"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "获取故事详情失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 切换点赞状态
     */
    suspend fun toggleLike(storyId: Long): Result<Boolean> {
        return try {
            val token = tokenRepository.getToken().first()
            if (token.isNullOrBlank()) {
                return Result.failure(Exception("请先登录"))
            }
            val response = communityApiService.toggleLike(
                storyId = storyId,
                token = token
            )
            if (response.isSuccessful && response.body()?.code == 200) {
                val isLiked = response.body()?.data?.get("isLiked") ?: false
                Result.success(isLiked)
            } else {
                Result.failure(Exception(response.body()?.message ?: "操作失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 获取用户发布的故事列表
     */
    suspend fun getUserStories(
        userId: String,
        page: Int = 1,
        size: Int = 20
    ): Result<List<CommunityStoryModel>> {
        return try {
            val response = communityApiService.getUserStories(
                userId = userId,
                page = page,
                size = size
            )
            if (response.isSuccessful && response.body()?.code == 200) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "获取用户故事失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 发布故事
     */
    suspend fun publishStory(request: PublishStoryRequest): Result<CommunityStoryDetailModel> {
        return try {
            val token = tokenRepository.getToken().first()
            if (token.isNullOrBlank()) {
                return Result.failure(Exception("请先登录"))
            }
            val response = communityApiService.publishStory(
                request = request,
                token = token
            )
            if (response.isSuccessful && response.body()?.code == 200) {
                response.body()?.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("发布失败"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "发布故事失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 删除故事
     */
    suspend fun deleteStory(storyId: Long): Result<Unit> {
        return try {
            val token = tokenRepository.getToken().first()
            if (token.isNullOrBlank()) {
                return Result.failure(Exception("请先登录"))
            }
            val response = communityApiService.deleteStory(
                storyId = storyId,
                token = token
            )
            if (response.isSuccessful && response.body()?.code == 200) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "删除故事失败"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
