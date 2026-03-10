package com.sleepstory.backend.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 故事列表响应（用于发现页）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryListResponse {

    /**
     * 故事ID
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 分类
     */
    private String category;

    /**
     * 时长
     */
    private String duration;

    /**
     * 图标
     */
    private String icon;

    /**
     * 渐变色
     */
    private String gradientColors;

    /**
     * 评分
     */
    private Float rating;

    /**
     * 播放次数
     */
    private Integer playCount;
}
