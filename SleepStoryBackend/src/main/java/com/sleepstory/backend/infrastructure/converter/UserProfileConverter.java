package com.sleepstory.backend.infrastructure.converter;

import com.sleepstory.backend.dal.po.UserProfilePO;
import com.sleepstory.backend.domain.entity.UserProfile;
import com.sleepstory.backend.domain.valueobject.StoryDuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserProfile 实体与 PO 转换器
 */
@Component
public class UserProfileConverter {

    private static final String CATEGORY_SEPARATOR = ",";

    public UserProfile toEntity(UserProfilePO po) {
        if (po == null) {
            return null;
        }
        return UserProfile.builder()
                .userId(po.getUserId())
                .sleepTime(po.getSleepTime())
                .preferredDuration(po.getPreferredDuration() != null ?
                        StoryDuration.valueOf(po.getPreferredDuration()) : null)
                .preferredCategories(parseCategories(po.getPreferredCategories()))
                .streakDays(po.getStreakDays())
                .totalStoriesListened(po.getTotalStoriesListened())
                .totalListeningMinutes(po.getTotalListeningMinutes())
                .lastListenedAt(po.getLastListenedAt())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }

    public UserProfilePO toPO(UserProfile entity) {
        if (entity == null) {
            return null;
        }
        return UserProfilePO.builder()
                .userId(entity.getUserId())
                .sleepTime(entity.getSleepTime())
                .preferredDuration(entity.getPreferredDuration() != null ?
                        entity.getPreferredDuration().name() : null)
                .preferredCategories(joinCategories(entity.getPreferredCategories()))
                .streakDays(entity.getStreakDays())
                .totalStoriesListened(entity.getTotalStoriesListened())
                .totalListeningMinutes(entity.getTotalListeningMinutes())
                .lastListenedAt(entity.getLastListenedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private List<String> parseCategories(String categoriesStr) {
        if (!StringUtils.hasText(categoriesStr)) {
            return Collections.emptyList();
        }
        return Arrays.stream(categoriesStr.split(CATEGORY_SEPARATOR))
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    private String joinCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        return String.join(CATEGORY_SEPARATOR, categories);
    }
}
