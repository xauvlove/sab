package com.sleepstory.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// User Profile
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "晚安旅人",
    val sleepTime: String = "22:30",
    val preferredDuration: StoryDuration = StoryDuration.MEDIUM,
    val preferredCategories: List<StoryCategory> = emptyList(),
    val onboardingCompleted: Boolean = false,
    val streakDays: Int = 0,
    val totalStoriesListened: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

// Story
@Entity(tableName = "stories")
data class Story(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: StoryCategory,
    val duration: Int, // in minutes
    val durationSeconds: Int = 0,
    val icon: String,
    val gradientColors: List<String>,
    val audioUrl: String? = null,
    val isGenerated: Boolean = false,
    val isFavorite: Boolean = false,
    val rating: Float = 0f,
    val playCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

// Listening History
@Entity(tableName = "listening_history")
data class ListeningHistory(
    @PrimaryKey val id: String,
    val storyId: String,
    val storyTitle: String,
    val listenedAt: Long,
    val durationListened: Int, // in seconds
    val completed: Boolean = false
)

// Sleep Record
@Entity(tableName = "sleep_records")
data class SleepRecord(
    @PrimaryKey val id: String,
    val date: String,
    val sleepTime: Long,
    val wakeTime: Long? = null,
    val sleepQuality: Int? = null, // 1-5
    val storyId: String? = null,
    val notes: String? = null
)

// Story Generation Request
@Entity(tableName = "generation_requests")
data class StoryGenerationRequest(
    @PrimaryKey val id: String,
    val keywords: String,
    val scene: StoryScene,
    val mood: StoryMood,
    val duration: StoryDuration,
    val status: GenerationStatus = GenerationStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val resultStoryId: String? = null
)

// Enums
enum class StoryCategory {
    NATURE,      // 自然
    FANTASY,     // 奇幻
    MEDITATION,  // 冥想
    SCIFI,       // 科幻
    CLASSIC,     // 经典
    WARM         // 温暖
}

enum class StoryDuration {
    SHORT,   // 5-10 minutes
    MEDIUM,  // 15-30 minutes
    LONG     // 30-60 minutes
}

enum class StoryScene {
    MOONLIGHT,  // 月夜
    MOUNTAIN,   // 山间
    BEACH,      // 海边
    COTTAGE,    // 小屋
    JOURNEY,    // 旅途
    GARDEN      // 花园
}

enum class StoryMood {
    CALM,   // 平静
    WARM,   // 温暖
    FANTASY // 奇幻
}

enum class GenerationStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}

// Assessment Questions
data class AssessmentQuestion(
    val id: Int,
    val question: String,
    val subtitle: String,
    val options: List<AssessmentOption>
)

data class AssessmentOption(
    val id: String,
    val text: String,
    val icon: String? = null
)

// Story Content for AI Generation
data class StoryContent(
    val title: String,
    val content: String,
    val paragraphs: List<String>
)