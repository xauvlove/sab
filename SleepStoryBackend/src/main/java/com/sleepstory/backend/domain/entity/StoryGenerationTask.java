package com.sleepstory.backend.domain.entity;

import com.sleepstory.backend.domain.valueobject.GenerationStatus;
import com.sleepstory.backend.domain.valueobject.StoryDuration;
import com.sleepstory.backend.domain.valueobject.StoryMood;
import com.sleepstory.backend.domain.valueobject.StoryScene;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 故事生成任务实体 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryGenerationTask {

    private String id;

    private String userId;

    private String keywords;

    private StoryScene scene;

    private StoryMood mood;

    private StoryDuration duration;

    private GenerationStatus status;

    private String storyId;

    private String errorMessage;

    private Integer progressPercent;

    private String statusMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    public void startProcessing() {
        this.status = GenerationStatus.PROCESSING;
        this.progressPercent = 0;
        this.statusMessage = "开始生成故事...";
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProgress(int percent, String message) {
        this.progressPercent = percent;
        this.statusMessage = message;
        this.updatedAt = LocalDateTime.now();
    }

    public void markCompleted(String storyId) {
        this.status = GenerationStatus.COMPLETED;
        this.storyId = storyId;
        this.progressPercent = 100;
        this.statusMessage = "故事生成完成";
        this.completedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markFailed(String errorMessage) {
        this.status = GenerationStatus.FAILED;
        this.errorMessage = errorMessage;
        this.statusMessage = "生成失败: " + errorMessage;
        this.updatedAt = LocalDateTime.now();
    }
}
