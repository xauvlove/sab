package com.sleepstory.backend.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 他人创作故事实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityStory {

    /**
     * 故事ID
     */
    private Long id;

    /**
     * 创作者用户ID
     */
    private String userId;

    /**
     * 故事标题
     */
    private String title;

    /**
     * 故事简介
     */
    private String summary;

    /**
     * 故事正文内容
     */
    private String content;

    /**
     * 语音配置（JSON格式）
     */
    private String voiceConfig;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 状态：0-草稿，1-已发布，2-已下架
     */
    private Integer status;

    /**
     * 点赞数
     */
    private Integer likesCount;

    /**
     * 播放次数
     */
    private Integer playsCount;

    /**
     * 创作者昵称
     */
    private String userNickname;

    /**
     * 创作者头像
     */
    private String userAvatar;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
