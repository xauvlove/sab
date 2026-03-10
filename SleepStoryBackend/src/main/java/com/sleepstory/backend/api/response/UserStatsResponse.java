package com.sleepstory.backend.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户统计数据响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsResponse {

    /**
     * 累计听书时长（分钟）
     */
    private Integer totalListeningMinutes;

    /**
     * 听完故事数
     */
    private Integer totalStoriesListened;

    /**
     * 连续入睡天数
     */
    private Integer streakDays;

    /**
     * 入睡成功率（百分比）
     */
    private Float successRate;

    /**
     * 本周听书时长统计（按天）
     */
    private List<DailyStats> weeklyStats;

    /**
     * 每日统计
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyStats {

        /**
         * 日期
         */
        private String date;

        /**
         * 听书分钟数
         */
        private Integer minutes;
    }
}
