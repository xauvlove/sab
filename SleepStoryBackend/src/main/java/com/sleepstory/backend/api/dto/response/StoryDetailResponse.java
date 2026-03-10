package com.sleepstory.backend.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 故事详情响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryDetailResponse {

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
     * 时长（秒）
     */
    private Integer durationSeconds;

    /**
     * 图标
     */
    private String icon;

    /**
     * 渐变色
     */
    private String gradientColors;

    /**
     * 音频URL
     */
    private String audioUrl;

    /**
     * 评分
     */
    private Float rating;

    /**
     * 播放次数
     */
    private Integer playCount;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否已收藏
     */
    private Boolean isFavorited;

    /**
     * 标签列表
     */
    private List<String> tags;
}
