package com.sleepstory.backend.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 故事详情响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityStoryDetailResponse {

    /**
     * 故事ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String summary;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 语音配置
     */
    private String voiceConfig;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private String tags;

    /**
     * 点赞数
     */
    private Integer likesCount;

    /**
     * 播放次数
     */
    private Integer playsCount;

    /**
     * 创作者ID
     */
    private String userId;

    /**
     * 创作者昵称
     */
    private String userNickname;

    /**
     * 创作者头像
     */
    private String userAvatar;

    /**
     * 是否已点赞
     */
    private Boolean isLiked;

    /**
     * 是否为自己的故事
     */
    private Boolean isOwner;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
