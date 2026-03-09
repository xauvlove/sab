package com.sleepstory.backend.infrastructure.repository;

import com.sleepstory.backend.dal.mapper.UserMapper;
import com.sleepstory.backend.dal.po.UserPO;
import com.sleepstory.backend.domain.entity.User;
import com.sleepstory.backend.domain.repository.UserRepository;
import com.sleepstory.backend.infrastructure.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 用户仓储实现 - Infrastructure层
 * 使用MyBatis Mapper实现领域仓储接口
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;
    private final UserConverter userConverter;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            UserPO po = userConverter.toPO(user);
            userMapper.insert(po);
        } else {
            user.setUpdatedAt(LocalDateTime.now());
            UserPO po = userConverter.toPO(user);
            userMapper.update(po);
        }
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        UserPO po = userMapper.selectById(id);
        return Optional.ofNullable(po).map(userConverter::toEntity);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        UserPO po = userMapper.selectByPhone(phone);
        return Optional.ofNullable(po).map(userConverter::toEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        UserPO po = userMapper.selectByEmail(email);
        return Optional.ofNullable(po).map(userConverter::toEntity);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userMapper.countByPhone(phone) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    @Override
    public void updateStatus(String id, String status) {
        userMapper.updateStatus(id, status);
    }

    @Override
    public void updateLoginInfo(String id, LocalDateTime loginAt, String loginIp) {
        userMapper.updateLoginInfo(id, loginAt, loginIp);
    }

    @Override
    public void deleteById(String id) {
        userMapper.deleteById(id);
    }
}
