package com.sleepstory.backend.domain.valueobject;

import lombok.Getter;

/**
 * 用户状态值对象
 */
@Getter
public enum UserStatus {
    PENDING("待激活", "用户已注册但未激活"),
    ACTIVE("正常", "用户正常使用中"),
    LOCKED("已锁定", "用户被锁定，无法登录"),
    DISABLED("已禁用", "用户被禁用");

    private final String displayName;
    private final String description;

    UserStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}
