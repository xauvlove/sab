package com.sleepstory.backend.dal.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户资料数据对象 - DAL层
 * 对应数据库表 user_profiles
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfilePO {

    private String userId;

    private String sleepTime;

    private String preferredDuration;

    private String preferredCategories;

    private Integer streakDays;

    private Integer totalStoriesListened;

    private Integer totalListeningMinutes;

    private LocalDateTime lastListenedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
