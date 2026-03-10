package com.sleepstory.backend.domain.repository;

import com.sleepstory.backend.domain.entity.PlayHistory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 播放历史仓储接口 - 领域层
 * 定义领域对象的持久化操作契约
 */
public interface PlayHistoryRepository {

    /**
     * 保存播放历史
     *
     * @param playHistory 播放历史实体
     * @return 保存后的播放历史
     */
    PlayHistory save(PlayHistory playHistory);

    /**
     * 根据用户ID查询播放历史
     *
     * @param userId 用户ID
     * @param limit  限制数量
     * @param offset 偏移量
     * @return 播放历史列表
     */
    List<PlayHistory> findByUserId(String userId, int limit, int offset);

    /**
     * 根据ID查询播放历史
     *
     * @param id 播放历史ID
     * @return 播放历史（如果存在）
     */
    Optional<PlayHistory> findById(String id);

    /**
     * 获取本周播放统计
     *
     * @param userId 用户ID
     * @return 每周统计数据列表
     */
    List<Map<String, Object>> findWeeklyStats(String userId);

    /**
     * 获取总播放时长（分钟）
     *
     * @param userId 用户ID
     * @return 总播放时长
     */
    Integer getTotalMinutes(String userId);

    /**
     * 获取播放故事数量
     *
     * @param userId 用户ID
     * @return 播放故事数量
     */
    Integer getTotalStoriesCount(String userId);

    /**
     * 获取连续播放天数
     *
     * @param userId 用户ID
     * @return 连续播放天数
     */
    Integer getStreakDays(String userId);

    /**
     * 删除用户的播放历史
     *
     * @param userId 用户ID
     */
    void deleteByUserId(String userId);
}
