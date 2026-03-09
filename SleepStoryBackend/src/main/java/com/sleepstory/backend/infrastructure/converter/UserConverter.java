package com.sleepstory.backend.infrastructure.converter;

import com.sleepstory.backend.dal.po.UserPO;
import com.sleepstory.backend.domain.entity.User;
import com.sleepstory.backend.domain.valueobject.UserStatus;
import org.springframework.stereotype.Component;

/**
 * User 实体与 PO 转换器
 */
@Component
public class UserConverter {

    public User toEntity(UserPO po) {
        if (po == null) {
            return null;
        }
        return User.builder()
                .id(po.getId())
                .phone(po.getPhone())
                .email(po.getEmail())
                .passwordHash(po.getPasswordHash())
                .nickname(po.getNickname())
                .avatarUrl(po.getAvatarUrl())
                .status(po.getStatus() != null ? UserStatus.valueOf(po.getStatus()) : null)
                .lastLoginAt(po.getLastLoginAt())
                .lastLoginIp(po.getLastLoginIp())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }

    public UserPO toPO(User entity) {
        if (entity == null) {
            return null;
        }
        return UserPO.builder()
                .id(entity.getId())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .nickname(entity.getNickname())
                .avatarUrl(entity.getAvatarUrl())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .lastLoginAt(entity.getLastLoginAt())
                .lastLoginIp(entity.getLastLoginIp())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
