package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.domain.entity.CommunityStory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 他人创作故事数据访问接口
 */
@Mapper
public interface CommunityStoryMapper {

    /**
     * 插入故事
     */
    int insert(CommunityStory story);

    /**
     * 根据ID查询故事
     */
    CommunityStory selectById(@Param("id") Long id);

    /**
     * 查询故事列表（分页）
     */
    List<Map<String, Object>> selectList(
            @Param("status") Integer status,
            @Param("category") String category,
            @Param("orderBy") String orderBy,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    /**
     * 查询用户发布的故事列表
     */
    List<Map<String, Object>> selectByUserId(
            @Param("userId") String userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    /**
     * 增加播放次数
     */
    int incrementPlaysCount(@Param("id") Long id);

    /**
     * 增加点赞数
     */
    int incrementLikesCount(@Param("id") Long id);

    /**
     * 减少点赞数
     */
    int decrementLikesCount(@Param("id") Long id);

    /**
     * 更新故事
     */
    int update(CommunityStory story);

    /**
     * 删除故事
     */
    int deleteById(@Param("id") Long id);

    /**
     * 检查是否已点赞
     */
    Integer checkLiked(@Param("userId") String userId, @Param("storyId") Long storyId);

    /**
     * 插入点赞记录
     */
    int insertLike(@Param("userId") String userId, @Param("storyId") Long storyId);

    /**
     * 删除点赞记录
     */
    int deleteLike(@Param("userId") String userId, @Param("storyId") Long storyId);

    /**
     * 统计故事总数
     */
    int count(@Param("status") Integer status, @Param("category") String category);
}
