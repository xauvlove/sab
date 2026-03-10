package com.sleepstory.backend.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 故事列表响应（用于Feed流）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityStoryResponse {

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
     * 分类
     */
    private String category;

    /**
     * 标签列表
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
     * 创建时间
     */
    private LocalDateTime createdAt;
}
