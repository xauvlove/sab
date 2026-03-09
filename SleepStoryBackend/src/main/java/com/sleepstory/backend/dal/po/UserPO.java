package com.sleepstory.backend.dal.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户数据对象 - DAL层
 * 对应数据库表 users
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPO {

    private String id;

    private String phone;

    private String email;

    private String passwordHash;

    private String nickname;

    private String avatarUrl;

    private String status;

    private LocalDateTime lastLoginAt;

    private String lastLoginIp;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
