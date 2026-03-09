package com.sleepstory.backend.infrastructure.repository;

import com.sleepstory.backend.dal.mapper.UserProfileMapper;
import com.sleepstory.backend.dal.po.UserProfilePO;
import com.sleepstory.backend.domain.entity.UserProfile;
import com.sleepstory.backend.domain.repository.UserProfileRepository;
import com.sleepstory.backend.infrastructure.converter.UserProfileConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户资料仓储实现 - Infrastructure层
 * 使用MyBatis Mapper实现领域仓储接口
 */
@Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImpl implements UserProfileRepository {

    private final UserProfileMapper userProfileMapper;
    private final UserProfileConverter userProfileConverter;

    @Override
    public UserProfile save(UserProfile profile) {
        UserProfilePO existingPo = userProfileMapper.selectByUserId(profile.getUserId());
        if (existingPo == null) {
            profile.setCreatedAt(LocalDateTime.now());
            profile.setUpdatedAt(LocalDateTime.now());
            profile.setStreakDays(0);
            profile.setTotalStoriesListened(0);
            profile.setTotalListeningMinutes(0);
            UserProfilePO po = userProfileConverter.toPO(profile);
            userProfileMapper.insert(po);
        } else {
            profile.setUpdatedAt(LocalDateTime.now());
            UserProfilePO po = userProfileConverter.toPO(profile);
            userProfileMapper.update(po);
        }
        return profile;
    }

    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        UserProfilePO po = userProfileMapper.selectByUserId(userId);
        return Optional.ofNullable(po).map(userProfileConverter::toEntity);
    }

    @Override
    public void incrementStreak(String userId) {
        userProfileMapper.incrementStreak(userId);
    }

    @Override
    public void resetStreak(String userId) {
        userProfileMapper.resetStreak(userId);
    }

    @Override
    public void recordListening(String userId, int minutes) {
        userProfileMapper.recordListening(userId, minutes);
    }

    @Override
    public void deleteByUserId(String userId) {
        userProfileMapper.deleteByUserId(userId);
    }
}
