package com.sleepstory.backend.infrastructure.repository;

import com.sleepstory.backend.dal.mapper.CommunityStoryMapper;
import com.sleepstory.backend.domain.entity.CommunityStory;
import com.sleepstory.backend.domain.repository.CommunityStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 社区故事仓储实现 - Infrastructure层
 * 使用MyBatis Mapper实现领域仓储接口
 */
@Repository
@RequiredArgsConstructor
public class CommunityStoryRepositoryImpl implements CommunityStoryRepository {

    private final CommunityStoryMapper communityStoryMapper;

    @Override
    public CommunityStory save(CommunityStory story) {
        if (story.getId() == null) {
            // 新增
            story.setCreatedAt(LocalDateTime.now());
            story.setUpdatedAt(LocalDateTime.now());
            if (story.getLikesCount() == null) {
                story.setLikesCount(0);
            }
            if (story.getPlaysCount() == null) {
                story.setPlaysCount(0);
            }
            if (story.getStatus() == null) {
                story.setStatus(1); // 已发布
            }
            communityStoryMapper.insert(story);
        } else {
            // 更新
            story.setUpdatedAt(LocalDateTime.now());
            communityStoryMapper.update(story);
        }
        return story;
    }

    @Override
    public Optional<CommunityStory> findById(Long id) {
        CommunityStory story = communityStoryMapper.selectById(id);
        return Optional.ofNullable(story);
    }

    @Override
    public List<CommunityStory> findList(Integer status, String category, String orderBy, int offset, int limit) {
        List<Map<String, Object>> rawList = communityStoryMapper.selectList(status, category, orderBy, offset, limit);
        
        return rawList.stream()
                .map(this::mapToCommunityStory)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommunityStory> findByUserId(String userId, int offset, int limit) {
        List<Map<String, Object>> rawList = communityStoryMapper.selectByUserId(userId, offset, limit);
        
        return rawList.stream()
                .map(this::mapToCommunityStory)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementPlaysCount(Long id) {
        communityStoryMapper.incrementPlaysCount(id);
    }

    @Override
    public void incrementLikesCount(Long id) {
        communityStoryMapper.incrementLikesCount(id);
    }

    @Override
    public void decrementLikesCount(Long id) {
        communityStoryMapper.decrementLikesCount(id);
    }

    @Override
    public void deleteById(Long id) {
        communityStoryMapper.deleteById(id);
    }

    @Override
    public boolean checkLiked(String userId, Long storyId) {
        Integer liked = communityStoryMapper.checkLiked(userId, storyId);
        return liked != null && liked > 0;
    }

    @Override
    public void insertLike(String userId, Long storyId) {
        communityStoryMapper.insertLike(userId, storyId);
    }

    @Override
    public void deleteLike(String userId, Long storyId) {
        communityStoryMapper.deleteLike(userId, storyId);
    }

    @Override
    public int count(Integer status, String category) {
        return communityStoryMapper.count(status, category);
    }

    /**
     * 将Map转换为CommunityStory实体
     */
    private CommunityStory mapToCommunityStory(Map<String, Object> item) {
        return CommunityStory.builder()
                .id(((Number) item.get("id")).longValue())
                .title((String) item.get("title"))
                .summary((String) item.get("summary"))
                .category((String) item.get("category"))
                .tags((String) item.get("tags"))
                .likesCount(((Number) item.get("likesCount")).intValue())
                .playsCount(((Number) item.get("playsCount")).intValue())
                .userNickname((String) item.get("userNickname"))
                .userAvatar((String) item.get("userAvatar"))
                .createdAt(item.get("createdAt") != null ? 
                    ((java.sql.Timestamp) item.get("createdAt")).toLocalDateTime() : null)
                .build();
    }
}
