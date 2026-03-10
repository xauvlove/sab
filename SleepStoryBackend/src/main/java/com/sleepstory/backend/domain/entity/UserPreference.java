package com.sleepstory.backend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户偏好设置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 深色模式
     */
    @Builder.Default
    private Boolean darkMode = false;

    /**
     * 自动关闭定时器（分钟）
     */
    @Builder.Default
    private Integer autoCloseTimer = 30;

    /**
     * 音量等级
     */
    @Builder.Default
    private Integer volumeLevel = 50;

    /**
     * 默认播放速度
     */
    @Builder.Default
    private Float defaultPlaySpeed = 1.0f;

    /**
     * 开启通知
     */
    @Builder.Default
    private Boolean enableNotifications = true;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
