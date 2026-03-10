package com.sleepstory.backend.infrastructure.repository;

import com.sleepstory.backend.dal.mapper.UserPreferenceMapper;
import com.sleepstory.backend.dal.po.UserPreferencePO;
import com.sleepstory.backend.domain.entity.UserPreference;
import com.sleepstory.backend.domain.repository.UserPreferenceRepository;
import com.sleepstory.backend.infrastructure.converter.UserPreferenceConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户偏好设置仓储实现 - Infrastructure层
 * 使用MyBatis Mapper实现领域仓储接口
 */
@Repository
@RequiredArgsConstructor
public class UserPreferenceRepositoryImpl implements UserPreferenceRepository {

    private final UserPreferenceMapper userPreferenceMapper;
    private final UserPreferenceConverter userPreferenceConverter;

    @Override
    public UserPreference save(UserPreference preference) {
        UserPreferencePO existingPo = userPreferenceMapper.selectByUserId(preference.getUserId());

        if (existingPo == null) {
            // 新建
            preference.setCreatedAt(LocalDateTime.now());
            preference.setUpdatedAt(LocalDateTime.now());
            UserPreferencePO po = userPreferenceConverter.toPO(preference);
            userPreferenceMapper.insert(po);
        } else {
            // 更新
            preference.setUpdatedAt(LocalDateTime.now());
            UserPreferencePO po = userPreferenceConverter.toPOForUpdate(preference, existingPo.getCreatedAt());
            userPreferenceMapper.update(po);
        }

        return preference;
    }

    @Override
    public Optional<UserPreference> findByUserId(String userId) {
        UserPreferencePO po = userPreferenceMapper.selectByUserId(userId);
        return Optional.ofNullable(po).map(userPreferenceConverter::toEntity);
    }

    @Override
    public void deleteByUserId(String userId) {
        userPreferenceMapper.deleteByUserId(userId);
    }
}
