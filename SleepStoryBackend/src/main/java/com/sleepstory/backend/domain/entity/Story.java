package com.sleepstory.backend.domain.entity;

import com.sleepstory.backend.domain.valueobject.StoryCategory;
import com.sleepstory.backend.domain.valueobject.StoryDuration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 故事实体 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Story {

    private String id;

    private String title;

    private String description;

    private StoryCategory category;

    private StoryDuration duration;

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

    public void incrementPlayCount() {
        if (this.playCount == null) {
            this.playCount = 0;
        }
        this.playCount++;
    }

    public void toggleFavorite() {
        this.isFavorite = !Boolean.TRUE.equals(this.isFavorite);
    }
}
