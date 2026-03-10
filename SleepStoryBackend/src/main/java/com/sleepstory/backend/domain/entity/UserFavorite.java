package com.sleepstory.backend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户收藏
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFavorite {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 故事ID
     */
    private String storyId;

    /**
     * 收藏时间
     */
    private LocalDateTime createdAt;
}
