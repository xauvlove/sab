package com.sleepstory.backend.dal.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 故事生成任务数据对象 - DAL层
 * 对应数据库表 story_generation_tasks
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryGenerationTaskPO {

    private String id;

    private String userId;

    private String keywords;

    private String scene;

    private String mood;

    private String duration;

    private String status;

    private String storyId;

    private String errorMessage;

    private Integer progressPercent;

    private String statusMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;
}
