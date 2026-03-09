package com.sleepstory.backend.domain.entity;

import com.sleepstory.backend.domain.valueobject.StoryDuration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户资料实体 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private String userId;

    private String sleepTime;

    private StoryDuration preferredDuration;

    private List<String> preferredCategories;

    private Integer streakDays;

    private Integer totalStoriesListened;

    private Integer totalListeningMinutes;

    private LocalDateTime lastListenedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 增加连续天数
     */
    public void incrementStreak() {
        if (this.streakDays == null) {
            this.streakDays = 0;
        }
        this.streakDays++;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 重置连续天数
     */
    public void resetStreak() {
        this.streakDays = 0;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 记录故事收听
     */
    public void recordListening(int minutes) {
        if (this.totalStoriesListened == null) {
            this.totalStoriesListened = 0;
        }
        if (this.totalListeningMinutes == null) {
            this.totalListeningMinutes = 0;
        }
        this.totalStoriesListened++;
        this.totalListeningMinutes += minutes;
        this.lastListenedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
