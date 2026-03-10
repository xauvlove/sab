package com.sleepstory.backend.domain.repository;

import com.sleepstory.backend.domain.entity.CommunityStory;

import java.util.List;
import java.util.Optional;

/**
 * 社区故事仓储接口 - 领域层
 * 定义社区故事领域对象的持久化操作契约
 */
public interface CommunityStoryRepository {

    /**
     * 保存故事（新增或更新）
     */
    CommunityStory save(CommunityStory story);

    /**
     * 根据ID查询故事
     */
    Optional<CommunityStory> findById(Long id);

    /**
     * 查询故事列表（分页）
     */
    List<CommunityStory> findList(Integer status, String category, String orderBy, int offset, int limit);

    /**
     * 查询用户发布的故事列表
     */
    List<CommunityStory> findByUserId(String userId, int offset, int limit);

    /**
     * 增加播放次数
     */
    void incrementPlaysCount(Long id);

    /**
     * 增加点赞数
     */
    void incrementLikesCount(Long id);

    /**
     * 减少点赞数
     */
    void decrementLikesCount(Long id);

    /**
     * 删除故事
     */
    void deleteById(Long id);

    /**
     * 检查是否已点赞
     */
    boolean checkLiked(String userId, Long storyId);

    /**
     * 插入点赞记录
     */
    void insertLike(String userId, Long storyId);

    /**
     * 删除点赞记录
     */
    void deleteLike(String userId, Long storyId);

    /**
     * 统计故事总数
     */
    int count(Integer status, String category);
}
