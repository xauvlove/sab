package com.sleepstory.backend.infrastructure.converter;

import com.sleepstory.backend.dal.po.StoryPO;
import com.sleepstory.backend.domain.entity.Story;
import com.sleepstory.backend.domain.valueobject.StoryCategory;
import com.sleepstory.backend.domain.valueobject.StoryDuration;
import org.springframework.stereotype.Component;

/**
 * Story 实体与 PO 转换器
 */
@Component
public class StoryConverter {

    public Story toEntity(StoryPO po) {
        if (po == null) {
            return null;
        }
        return Story.builder()
                .id(po.getId())
                .title(po.getTitle())
                .description(po.getDescription())
                .category(po.getCategory() != null ? StoryCategory.valueOf(po.getCategory()) : null)
                .duration(po.getDuration() != null ? StoryDuration.valueOf(po.getDuration()) : null)
                .durationSeconds(po.getDurationSeconds())
                .icon(po.getIcon())
                .gradientColors(po.getGradientColors())
                .audioUrl(po.getAudioUrl())
                .isGenerated(po.getIsGenerated())
                .isFavorite(po.getIsFavorite())
                .rating(po.getRating())
                .playCount(po.getPlayCount())
                .content(po.getContent())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }

    public StoryPO toPO(Story entity) {
        if (entity == null) {
            return null;
        }
        return StoryPO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .category(entity.getCategory() != null ? entity.getCategory().name() : null)
                .duration(entity.getDuration() != null ? entity.getDuration().name() : null)
                .durationSeconds(entity.getDurationSeconds())
                .icon(entity.getIcon())
                .gradientColors(entity.getGradientColors())
                .audioUrl(entity.getAudioUrl())
                .isGenerated(entity.getIsGenerated())
                .isFavorite(entity.getIsFavorite())
                .rating(entity.getRating())
                .playCount(entity.getPlayCount())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
