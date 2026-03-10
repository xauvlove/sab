package com.sleepstory.backend.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户偏好设置响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceResponse {

    /**
     * 深色模式
     */
    private Boolean darkMode;

    /**
     * 自动关闭定时器（分钟）
     */
    private Integer autoCloseTimer;

    /**
     * 音量等级
     */
    private Integer volumeLevel;

    /**
     * 默认播放速度
     */
    private Float defaultPlaySpeed;

    /**
     * 开启通知
     */
    private Boolean enableNotifications;
}
