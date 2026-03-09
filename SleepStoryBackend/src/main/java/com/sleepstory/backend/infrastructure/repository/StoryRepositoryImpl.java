package com.sleepstory.backend.infrastructure.repository;

import com.sleepstory.backend.dal.mapper.StoryMapper;
import com.sleepstory.backend.dal.po.StoryPO;
import com.sleepstory.backend.domain.entity.Story;
import com.sleepstory.backend.domain.repository.StoryRepository;
import com.sleepstory.backend.domain.valueobject.StoryCategory;
import com.sleepstory.backend.infrastructure.converter.StoryConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 故事仓储实现 - Infrastructure层
 * 使用MyBatis Mapper实现领域仓储接口
 */
@Repository
@RequiredArgsConstructor
public class StoryRepositoryImpl implements StoryRepository {

    private final StoryMapper storyMapper;
    private final StoryConverter storyConverter;

    @Override
    public Story save(Story story) {
        if (story.getId() == null) {
            story.setId(UUID.randomUUID().toString());
            story.setCreatedAt(LocalDateTime.now());
            story.setUpdatedAt(LocalDateTime.now());
            story.setPlayCount(0);
            story.setIsFavorite(false);
            StoryPO po = storyConverter.toPO(story);
            storyMapper.insert(po);
        } else {
            story.setUpdatedAt(LocalDateTime.now());
            StoryPO po = storyConverter.toPO(story);
            storyMapper.update(po);
        }
        return story;
    }

    @Override
    public Optional<Story> findById(String id) {
        StoryPO po = storyMapper.selectById(id);
        return Optional.ofNullable(po).map(storyConverter::toEntity);
    }

    @Override
    public List<Story> findByCategory(StoryCategory category) {
        List<StoryPO> pos = storyMapper.selectByCategory(category.name());
        return pos.stream()
                .map(storyConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Story> findRecommendedStories(int limit) {
        List<StoryPO> pos = storyMapper.selectRecommendedStories(limit);
        return pos.stream()
                .map(storyConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Story> findTopStories(int limit) {
        List<StoryPO> pos = storyMapper.selectTopStories(limit);
        return pos.stream()
                .map(storyConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Story> searchByKeyword(String keyword) {
        List<StoryPO> pos = storyMapper.searchByKeyword(keyword);
        return pos.stream()
                .map(storyConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Story> findByIsFavoriteTrue() {
        List<StoryPO> pos = storyMapper.selectFavorites();
        return pos.stream()
                .map(storyConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementPlayCount(String id) {
        storyMapper.incrementPlayCount(id);
    }

    @Override
    public void updateFavoriteStatus(String id, boolean isFavorite) {
        storyMapper.updateFavoriteStatus(id, isFavorite);
    }

    @Override
    public void deleteById(String id) {
        storyMapper.deleteById(id);
    }
}
