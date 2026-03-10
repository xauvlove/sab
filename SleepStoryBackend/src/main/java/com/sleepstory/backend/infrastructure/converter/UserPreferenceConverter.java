package com.sleepstory.backend.infrastructure.converter;

import com.sleepstory.backend.dal.po.UserPreferencePO;
import com.sleepstory.backend.domain.entity.UserPreference;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 用户偏好设置转换器
 * 负责 PO 与 Entity 之间的相互转换
 */
@Component
public class UserPreferenceConverter {

    /**
     * PO 转 Entity
     */
    public UserPreference toEntity(UserPreferencePO po) {
        if (po == null) {
            return null;
        }
        return UserPreference.builder()
                .userId(po.getUserId())
                .darkMode(po.getDarkMode())
                .autoCloseTimer(po.getAutoCloseTimer())
                .volumeLevel(po.getVolumeLevel())
                .defaultPlaySpeed(po.getDefaultPlaySpeed())
                .enableNotifications(po.getEnableNotifications())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }

    /**
     * Entity 转 PO
     */
    public UserPreferencePO toPO(UserPreference entity) {
        if (entity == null) {
            return null;
        }
        return UserPreferencePO.builder()
                .userId(entity.getUserId())
                .darkMode(entity.getDarkMode())
                .autoCloseTimer(entity.getAutoCloseTimer())
                .volumeLevel(entity.getVolumeLevel())
                .defaultPlaySpeed(entity.getDefaultPlaySpeed())
                .enableNotifications(entity.getEnableNotifications())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * 更新 PO（保留创建时间）
     */
    public UserPreferencePO toPOForUpdate(UserPreference entity, LocalDateTime originalCreatedAt) {
        if (entity == null) {
            return null;
        }
        return UserPreferencePO.builder()
                .userId(entity.getUserId())
                .darkMode(entity.getDarkMode())
                .autoCloseTimer(entity.getAutoCloseTimer())
                .volumeLevel(entity.getVolumeLevel())
                .defaultPlaySpeed(entity.getDefaultPlaySpeed())
                .enableNotifications(entity.getEnableNotifications())
                .createdAt(originalCreatedAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
