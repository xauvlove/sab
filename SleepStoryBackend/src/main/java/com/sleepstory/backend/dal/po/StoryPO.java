package com.sleepstory.backend.dal.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 故事数据对象 - DAL层
 * 对应数据库表 stories
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryPO {

    private String id;

    private String title;

    private String description;

    private String category;

    private String duration;

    private Integer durationSeconds;

    private String icon;

    private String gradientColors;

    private String audioUrl;

    private Boolean isGenerated;

    private Boolean isFavorite;

    private Float rating;

    private Integer playCount;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
