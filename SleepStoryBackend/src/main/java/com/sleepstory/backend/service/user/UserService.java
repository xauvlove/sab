package com.sleepstory.backend.service.user;

import com.sleepstory.backend.api.dto.request.UserPreferenceRequest;
import com.sleepstory.backend.api.dto.response.UserPreferenceResponse;
import com.sleepstory.backend.api.dto.response.UserStatsResponse;
import com.sleepstory.backend.dal.mapper.PlayHistoryMapper;
import com.sleepstory.backend.dal.mapper.UserPreferenceMapper;
import com.sleepstory.backend.dal.mapper.UserProfileMapper;
import com.sleepstory.backend.dal.po.UserPreferencePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务（统计和偏好设置）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserProfileMapper userProfileMapper;
    private final UserPreferenceMapper userPreferenceMapper;
    private final PlayHistoryMapper playHistoryMapper;

    /**
     * 获取用户睡眠统计数据
     */
    public UserStatsResponse getUserStats(String userId) {
        // 从用户资料中获取统计信息
        var userProfile = userProfileMapper.selectByUserId(userId);

        // 获取本周播放统计
        List<Map<String, Object>> weeklyStatsRaw = playHistoryMapper.selectWeeklyStats(userId);
        List<UserStatsResponse.DailyStats> weeklyStats = convertWeeklyStats(weeklyStatsRaw);

        // 计算入睡成功率（简化计算）
        Float successRate = calculateSuccessRate(userId);

        return UserStatsResponse.builder()
                .totalListeningMinutes(userProfile != null ? userProfile.getTotalListeningMinutes() : 0)
                .totalStoriesListened(userProfile != null ? userProfile.getTotalStoriesListened() : 0)
                .streakDays(userProfile != null ? userProfile.getStreakDays() : 0)
                .successRate(successRate)
                .weeklyStats(weeklyStats)
                .build();
    }

    /**
     * 获取用户偏好设置
     */
    public UserPreferenceResponse getUserPreferences(String userId) {
        UserPreferencePO preference = userPreferenceMapper.selectByUserId(userId);

        if (preference == null) {
            // 返回默认值
            return UserPreferenceResponse.builder()
                    .darkMode(false)
                    .autoCloseTimer(30)
                    .volumeLevel(50)
                    .defaultPlaySpeed(1.0f)
                    .enableNotifications(true)
                    .build();
        }

        return UserPreferenceResponse.builder()
                .darkMode(preference.getDarkMode())
                .autoCloseTimer(preference.getAutoCloseTimer())
                .volumeLevel(preference.getVolumeLevel())
                .defaultPlaySpeed(preference.getDefaultPlaySpeed())
                .enableNotifications(preference.getEnableNotifications())
                .build();
    }

    /**
     * 更新用户偏好设置
     */
    public UserPreferenceResponse updateUserPreferences(String userId, UserPreferenceRequest request) {
        UserPreferencePO preference = UserPreferencePO.builder()
                .userId(userId)
                .darkMode(request.getDarkMode() != null ? request.getDarkMode() : false)
                .autoCloseTimer(request.getAutoCloseTimer() != null ? request.getAutoCloseTimer() : 30)
                .volumeLevel(request.getVolumeLevel() != null ? request.getVolumeLevel() : 50)
                .defaultPlaySpeed(request.getDefaultPlaySpeed() != null ? request.getDefaultPlaySpeed() : 1.0f)
                .enableNotifications(request.getEnableNotifications() != null ? request.getEnableNotifications() : true)
                .build();

        userPreferenceMapper.upsert(preference);
        log.info("用户偏好设置已更新: userId={}", userId);

        return getUserPreferences(userId);
    }

    /**
     * 记录播放历史
     */
    public void recordPlayHistory(String userId, String storyId, int durationListened) {
        playHistoryMapper.insert(userId, storyId, durationListened);
        log.info("播放历史已记录: userId={}, storyId={}, duration={}s", userId, storyId, durationListened);

        // 更新用户资料中的统计信息
        var userProfile = userProfileMapper.selectByUserId(userId);
        if (userProfile != null) {
            int totalMinutes = userProfile.getTotalListeningMinutes() + durationListened / 60;
            int totalStories = userProfile.getTotalStoriesListened() + 1;
            userProfileMapper.updateTotalListeningMinutes(userId, totalMinutes);
            userProfileMapper.updateTotalStoriesListened(userId, totalStories);
        }
    }

    /**
     * 获取播放历史
     */
    public List<Map<String, Object>> getPlayHistory(String userId, int limit, int offset) {
        List<Map<String, Object>> rawHistory = playHistoryMapper.selectByUserId(userId, limit, offset);
        List<Map<String, Object>> history = new ArrayList<>();

        for (Map<String, Object> item : rawHistory) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("id", item.get("id"));
            entry.put("storyId", item.get("story_id"));
            entry.put("storyTitle", item.get("story_title"));
            entry.put("duration", item.get("duration_listened"));
            entry.put("completed", item.get("completed"));
            entry.put("playedAt", item.get("played_at"));
            history.add(entry);
        }

        return history;
    }

    /**
     * 计算入睡成功率
     */
    private Float calculateSuccessRate(String userId) {
        // 简化计算：基于连续天数和播放记录
        // 实际应该根据播放时长和入睡时间来计算
        Integer streakDays = playHistoryMapper.getStreakDays(userId);
        if (streakDays == null || streakDays == 0) {
            return 0f;
        }
        // 假设连续使用7天以上为高成功率
        return Math.min(100f, streakDays * 10f + 15f);
    }

    /**
     * 转换周统计数据
     */
    private List<UserStatsResponse.DailyStats> convertWeeklyStats(List<Map<String, Object>> rawStats) {
        List<UserStatsResponse.DailyStats> stats = new ArrayList<>();

        // 生成过去7天的数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 6; i >= 0; i--) {
            String date = LocalDate.now().minusDays(i).format(formatter);

            // 查找对应日期的统计数据
            int minutes = 0;
            for (Map<String, Object> raw : rawStats) {
                String statDate = raw.get("date").toString();
                if (statDate.equals(date)) {
                    Object totalSeconds = raw.get("totalSeconds");
                    if (totalSeconds != null) {
                        minutes = ((Number) totalSeconds).intValue() / 60;
                    }
                    break;
                }
            }

            stats.add(UserStatsResponse.DailyStats.builder()
                    .date(date.substring(5)) // 只保留月-日
                    .minutes(minutes)
                    .build());
        }

        return stats;
    }
}
