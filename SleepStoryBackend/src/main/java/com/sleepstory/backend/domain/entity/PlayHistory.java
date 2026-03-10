package com.sleepstory.backend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 播放历史
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayHistory {

    /**
     * ID
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 故事ID
     */
    private String storyId;

    /**
     * 收听时长（秒）
     */
    private Integer durationListened;

    /**
     * 播放时间
     */
    private LocalDateTime createdAt;
}
