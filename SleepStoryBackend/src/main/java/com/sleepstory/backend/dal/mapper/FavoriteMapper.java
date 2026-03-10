package com.sleepstory.backend.dal.mapper;

import com.sleepstory.backend.dal.po.StoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏数据访问接口
 */
@Mapper
public interface FavoriteMapper {

    /**
     * 添加收藏
     */
    int insert(@Param("userId") String userId, @Param("storyId") String storyId);

    /**
     * 取消收藏
     */
    int delete(@Param("userId") String userId, @Param("storyId") String storyId);

    /**
     * 检查是否已收藏
     */
    boolean exists(@Param("userId") String userId, @Param("storyId") String storyId);

    /**
     * 获取用户收藏列表
     */
    List<StoryPO> selectByUserId(@Param("userId") String userId);

    /**
     * 获取用户收藏数量
     */
    int countByUserId(@Param("userId") String userId);
}
