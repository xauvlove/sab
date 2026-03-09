package com.sleepstory.backend.infrastructure.converter;

import com.sleepstory.backend.dal.po.StoryGenerationTaskPO;
import com.sleepstory.backend.domain.entity.StoryGenerationTask;
import com.sleepstory.backend.domain.valueobject.GenerationStatus;
import com.sleepstory.backend.domain.valueobject.StoryDuration;
import com.sleepstory.backend.domain.valueobject.StoryMood;
import com.sleepstory.backend.domain.valueobject.StoryScene;
import org.springframework.stereotype.Component;

/**
 * StoryGenerationTask 实体与 PO 转换器
 */
@Component
public class StoryGenerationTaskConverter {

    public StoryGenerationTask toEntity(StoryGenerationTaskPO po) {
        if (po == null) {
            return null;
        }
        return StoryGenerationTask.builder()
                .id(po.getId())
                .userId(po.getUserId())
                .keywords(po.getKeywords())
                .scene(po.getScene() != null ? StoryScene.valueOf(po.getScene()) : null)
                .mood(po.getMood() != null ? StoryMood.valueOf(po.getMood()) : null)
                .duration(po.getDuration() != null ? StoryDuration.valueOf(po.getDuration()) : null)
                .status(po.getStatus() != null ? GenerationStatus.valueOf(po.getStatus()) : null)
                .storyId(po.getStoryId())
                .errorMessage(po.getErrorMessage())
                .progressPercent(po.getProgressPercent())
                .statusMessage(po.getStatusMessage())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .completedAt(po.getCompletedAt())
                .build();
    }

    public StoryGenerationTaskPO toPO(StoryGenerationTask entity) {
        if (entity == null) {
            return null;
        }
        return StoryGenerationTaskPO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .keywords(entity.getKeywords())
                .scene(entity.getScene() != null ? entity.getScene().name() : null)
                .mood(entity.getMood() != null ? entity.getMood().name() : null)
                .duration(entity.getDuration() != null ? entity.getDuration().name() : null)
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .storyId(entity.getStoryId())
                .errorMessage(entity.getErrorMessage())
                .progressPercent(entity.getProgressPercent())
                .statusMessage(entity.getStatusMessage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }
}
