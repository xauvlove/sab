package com.sleepstory.backend.domain.repository;

import com.sleepstory.backend.domain.entity.User;

import java.util.Optional;

/**
 * 用户仓储接口 - 领域层
 * 定义领域对象的持久化操作契约
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    void updateStatus(String id, String status);

    void updateLoginInfo(String id, java.time.LocalDateTime loginAt, String loginIp);

    void deleteById(String id);
}
