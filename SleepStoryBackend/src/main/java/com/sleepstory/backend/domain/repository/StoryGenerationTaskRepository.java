package com.sleepstory.backend.domain.repository;

import com.sleepstory.backend.domain.entity.StoryGenerationTask;
import com.sleepstory.backend.domain.valueobject.GenerationStatus;

import java.util.List;
import java.util.Optional;

/**
 * 故事生成任务仓储接口 - 领域层
 * 定义领域对象的持久化操作契约
 */
public interface StoryGenerationTaskRepository {

    StoryGenerationTask save(StoryGenerationTask task);

    Optional<StoryGenerationTask> findById(String id);

    List<StoryGenerationTask> findByUserIdOrderByCreatedAtDesc(String userId);

    List<StoryGenerationTask> findByStatus(GenerationStatus status);

    Optional<StoryGenerationTask> findTopByUserIdOrderByCreatedAtDesc(String userId);

    long countByStatus(GenerationStatus status);

    void deleteById(String id);
}
