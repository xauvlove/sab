package com.sleepstory.backend.infrastructure.repository;

import com.sleepstory.backend.dal.mapper.StoryGenerationTaskMapper;
import com.sleepstory.backend.dal.po.StoryGenerationTaskPO;
import com.sleepstory.backend.domain.entity.StoryGenerationTask;
import com.sleepstory.backend.domain.repository.StoryGenerationTaskRepository;
import com.sleepstory.backend.domain.valueobject.GenerationStatus;
import com.sleepstory.backend.infrastructure.converter.StoryGenerationTaskConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 故事生成任务仓储实现 - Infrastructure层
 * 使用MyBatis Mapper实现领域仓储接口
 */
@Repository
@RequiredArgsConstructor
public class StoryGenerationTaskRepositoryImpl implements StoryGenerationTaskRepository {

    private final StoryGenerationTaskMapper taskMapper;
    private final StoryGenerationTaskConverter taskConverter;

    @Override
    public StoryGenerationTask save(StoryGenerationTask task) {
        if (task.getId() == null) {
            task.setId(UUID.randomUUID().toString());
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            StoryGenerationTaskPO po = taskConverter.toPO(task);
            taskMapper.insert(po);
        } else {
            task.setUpdatedAt(LocalDateTime.now());
            StoryGenerationTaskPO po = taskConverter.toPO(task);
            taskMapper.update(po);
        }
        return task;
    }

    @Override
    public Optional<StoryGenerationTask> findById(String id) {
        StoryGenerationTaskPO po = taskMapper.selectById(id);
        return Optional.ofNullable(po).map(taskConverter::toEntity);
    }

    @Override
    public List<StoryGenerationTask> findByUserIdOrderByCreatedAtDesc(String userId) {
        List<StoryGenerationTaskPO> pos = taskMapper.selectByUserId(userId);
        return pos.stream()
                .map(taskConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoryGenerationTask> findByStatus(GenerationStatus status) {
        List<StoryGenerationTaskPO> pos = taskMapper.selectByStatus(status.name());
        return pos.stream()
                .map(taskConverter::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StoryGenerationTask> findTopByUserIdOrderByCreatedAtDesc(String userId) {
        StoryGenerationTaskPO po = taskMapper.selectLatestByUserId(userId);
        return Optional.ofNullable(po).map(taskConverter::toEntity);
    }

    @Override
    public long countByStatus(GenerationStatus status) {
        return taskMapper.countByStatus(status.name());
    }

    @Override
    public void deleteById(String id) {
        taskMapper.deleteById(id);
    }
}
