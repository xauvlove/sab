package com.sleepstory.backend.domain.repository;

import com.sleepstory.backend.domain.entity.UserProfile;

import java.util.Optional;

/**
 * 用户资料仓储接口 - 领域层
 * 定义领域对象的持久化操作契约
 */
public interface UserProfileRepository {

    UserProfile save(UserProfile profile);

    Optional<UserProfile> findByUserId(String userId);

    void incrementStreak(String userId);

    void resetStreak(String userId);

    void recordListening(String userId, int minutes);

    void deleteByUserId(String userId);
}
