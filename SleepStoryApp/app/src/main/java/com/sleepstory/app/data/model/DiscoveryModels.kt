package com.sleepstory.app.data.model

/**
 * 发现页数据模型
 */
data class DiscoveryResponse(
    val hotTags: List<TagModel>,
    val categories: List<CategoryModel>,
    val rankings: List<StoryModel>
)

data class TagModel(
    val id: Int,
    val name: String,
    val usageCount: Int
)

data class CategoryModel(
    val name: String,
    val icon: String,
    val storyCount: Int
)

/**
 * 故事模型（用于列表）
 */
data class StoryModel(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val duration: String,
    val icon: String,
    val gradientColors: String,
    val rating: Float,
    val playCount: Int
)

/**
 * 故事详情模型
 */
data class StoryDetailModel(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val duration: String,
    val durationSeconds: Int,
    val icon: String,
    val gradientColors: String,
    val audioUrl: String,
    val rating: Float,
    val playCount: Int,
    val content: String,
    val isFavorited: Boolean,
    val tags: List<String>
)

/**
 * 收藏操作响应
 */
data class FavoriteToggleResponse(
    val isFavorited: Boolean,
    val favoriteCount: Int
)

/**
 * 用户统计模型
 */
data class UserStatsModel(
    val totalListeningMinutes: Int,
    val totalStoriesListened: Int,
    val streakDays: Int,
    val successRate: Float,
    val weeklyStats: List<DailyStatsModel>
)

data class DailyStatsModel(
    val date: String,
    val minutes: Int
)

/**
 * 用户偏好设置模型
 */
data class UserPreferenceModel(
    val darkMode: Boolean,
    val autoCloseTimer: Int,
    val volumeLevel: Int,
    val defaultPlaySpeed: Float,
    val enableNotifications: Boolean
)
