package com.sleepstory.backend.dal.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户偏好设置数据对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencePO {

    private String userId;

    private Boolean darkMode;

    private Integer autoCloseTimer;

    private Integer volumeLevel;

    private Float defaultPlaySpeed;

    private Boolean enableNotifications;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
