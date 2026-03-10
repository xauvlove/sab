package com.sleepstory.backend.domain.repository;

import com.sleepstory.backend.domain.entity.UserPreference;

import java.util.Optional;

/**
 * 用户偏好设置仓储接口 - 领域层
 * 定义领域对象的持久化操作契约
 */
public interface UserPreferenceRepository {

    /**
     * 保存用户偏好设置
     *
     * @param preference 偏好设置实体
     * @return 保存后的偏好设置
     */
    UserPreference save(UserPreference preference);

    /**
     * 根据用户ID查询偏好设置
     *
     * @param userId 用户ID
     * @return 偏好设置（如果存在）
     */
    Optional<UserPreference> findByUserId(String userId);

    /**
     * 删除用户偏好设置
     *
     * @param userId 用户ID
     */
    void deleteByUserId(String userId);
}
