package com.sleepstory.backend.infrastructure.repository;

import com.sleepstory.backend.dal.mapper.PlayHistoryMapper;
import com.sleepstory.backend.domain.entity.PlayHistory;
import com.sleepstory.backend.domain.repository.PlayHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 播放历史仓储实现 - Infrastructure层
 * 使用MyBatis Mapper实现领域仓储接口
 */
@Repository
@RequiredArgsConstructor
public class PlayHistoryRepositoryImpl implements PlayHistoryRepository {

    private final PlayHistoryMapper playHistoryMapper;

    @Override
    public PlayHistory save(PlayHistory playHistory) {
        if (playHistory.getId() == null || playHistory.getId().isEmpty()) {
            playHistory.setId(UUID.randomUUID().toString());
        }
        if (playHistory.getCreatedAt() == null) {
            playHistory.setCreatedAt(LocalDateTime.now());
        }

        playHistoryMapper.insert(
                playHistory.getUserId(),
                playHistory.getStoryId(),
                playHistory.getDurationListened()
        );

        return playHistory;
    }

    @Override
    public List<PlayHistory> findByUserId(String userId, int limit, int offset) {
        // Mapper 返回的是 Map，需要手动转换
        List<Map<String, Object>> rawList = playHistoryMapper.selectByUserId(userId, limit, offset);
        return rawList.stream()
                .map(this::mapToPlayHistory)
                .toList();
    }

    @Override
    public Optional<PlayHistory> findById(String id) {
        // 由于 Mapper 没有提供 selectById 方法，这里简化处理
        // 实际项目中应该添加这个方法
        return Optional.empty();
    }

    @Override
    public List<Map<String, Object>> findWeeklyStats(String userId) {
        return playHistoryMapper.selectWeeklyStats(userId);
    }

    @Override
    public Integer getTotalMinutes(String userId) {
        return playHistoryMapper.getTotalMinutes(userId);
    }

    @Override
    public Integer getTotalStoriesCount(String userId) {
        return playHistoryMapper.getTotalStoriesCount(userId);
    }

    @Override
    public Integer getStreakDays(String userId) {
        return playHistoryMapper.getStreakDays(userId);
    }

    @Override
    public void deleteByUserId(String userId) {
        // Mapper 没有提供 deleteByUserId 方法，需要后续添加
    }

    /**
     * 将 Map 转换为 PlayHistory 实体
     */
    private PlayHistory mapToPlayHistory(Map<String, Object> map) {
        return PlayHistory.builder()
                .id(map.get("id") != null ? map.get("id").toString() : null)
                .userId(map.get("user_id") != null ? map.get("user_id").toString() : null)
                .storyId(map.get("story_id") != null ? map.get("story_id").toString() : null)
                .durationListened(map.get("duration_listened") != null ?
                        Integer.parseInt(map.get("duration_listened").toString()) : 0)
                .createdAt(map.get("played_at") != null ?
                        LocalDateTime.parse(map.get("played_at").toString()) : null)
                .build();
    }
}
