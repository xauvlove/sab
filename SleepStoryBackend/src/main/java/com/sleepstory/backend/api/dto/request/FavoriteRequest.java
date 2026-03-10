package com.sleepstory.backend.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequest {

    /**
     * 故事ID
     */
    private String storyId;
}
