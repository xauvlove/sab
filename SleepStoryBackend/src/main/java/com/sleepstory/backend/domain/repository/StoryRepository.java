package com.sleepstory.backend.domain.repository;

import com.sleepstory.backend.domain.entity.Story;
import com.sleepstory.backend.domain.valueobject.StoryCategory;

import java.util.List;
import java.util.Optional;

/**
 * 故事仓储接口 - 领域层
 * 定义领域对象的持久化操作契约
 */
public interface StoryRepository {

    Story save(Story story);

    Optional<Story> findById(String id);

    List<Story> findByCategory(StoryCategory category);

    List<Story> findRecommendedStories(int limit);

    List<Story> findTopStories(int limit);

    List<Story> searchByKeyword(String keyword);

    List<Story> findByIsFavoriteTrue();

    void incrementPlayCount(String id);

    void updateFavoriteStatus(String id, boolean isFavorite);

    void deleteById(String id);
}
