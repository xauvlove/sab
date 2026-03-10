package com.sleepstory.backend.dal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 播放历史数据访问接口
 */
@Mapper
public interface PlayHistoryMapper {

    /**
     * 添加播放记录
     */
    int insert(@Param("userId") String userId,
               @Param("storyId") String storyId,
               @Param("durationListened") Integer durationListened);

    /**
     * 获取用户播放历史
     */
    List<Map<String, Object>> selectByUserId(@Param("userId") String userId);

    /**
     * 获取本周播放统计
     */
    List<Map<String, Object>> selectWeeklyStats(@Param("userId") String userId);

    /**
     * 获取总播放时长（分钟）
     */
    Integer getTotalMinutes(@Param("userId") String userId);

    /**
     * 获取播放故事数量
     */
    Integer getTotalStoriesCount(@Param("userId") String userId);

    /**
     * 获取连续播放天数
     */
    Integer getStreakDays(@Param("userId") String userId);
}
