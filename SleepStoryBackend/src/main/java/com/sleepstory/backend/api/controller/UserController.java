package com.sleepstory.backend.api.controller;

import com.sleepstory.backend.api.dto.Result;
import com.sleepstory.backend.api.request.UserPreferenceRequest;
import com.sleepstory.backend.api.response.UserPreferenceResponse;
import com.sleepstory.backend.api.response.UserStatsResponse;
import com.sleepstory.backend.infrastructure.security.JwtTokenProvider;
import com.sleepstory.backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 获取用户睡眠统计
     * @param authHeader Authorization头（Bearer Token）
     * @return 用户统计信息（听书时长、连续天数、成功率等）
     */
    @GetMapping("/stats")
    public Result<UserStatsResponse> getUserStats(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            UserStatsResponse stats = userService.getUserStats(userId);
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取用户统计失败", e);
            return Result.error("获取统计失败");
        }
    }

    /**
     * 获取用户偏好设置
     * @param authHeader Authorization头（Bearer Token）
     * @return 用户偏好设置（深色模式、定时器、音量等）
     */
    @GetMapping("/preferences")
    public Result<UserPreferenceResponse> getPreferences(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            UserPreferenceResponse preferences = userService.getUserPreferences(userId);
            return Result.success(preferences);
        } catch (Exception e) {
            log.error("获取偏好设置失败", e);
            return Result.error("获取失败");
        }
    }

    /**
     * 更新用户偏好设置
     * @param request 偏好设置请求
     * @param authHeader Authorization头（Bearer Token）
     * @return 更新后的偏好设置
     */
    @PutMapping("/preferences")
    public Result<UserPreferenceResponse> updatePreferences(
            @Valid @RequestBody UserPreferenceRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            UserPreferenceResponse preferences = userService.updateUserPreferences(userId, request);
            return Result.success(preferences);
        } catch (Exception e) {
            log.error("更新偏好设置失败", e);
            return Result.error("更新失败");
        }
    }

    /**
     * 记录播放历史
     * @param request 播放历史请求（故事ID、播放时长）
     * @param authHeader Authorization头（Bearer Token）
     * @return 操作结果
     */
    @PostMapping("/play-history")
    public Result<Void> recordPlayHistory(
            @RequestBody Map<String, Object> request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            String storyId = (String) request.get("storyId");
            Integer duration = (Integer) request.get("duration");

            if (storyId == null || duration == null) {
                return Result.badRequest("参数不完整");
            }

            userService.recordPlayHistory(userId, storyId, duration);
            return Result.success(null);
        } catch (Exception e) {
            log.error("记录播放历史失败", e);
            return Result.error("记录失败");
        }
    }

    /**
     * 获取播放历史
     * @param authHeader Authorization头（Bearer Token）
     * @param limit 返回记录数（默认20）
     * @param offset 偏移量（默认0）
     * @return 播放历史列表
     */
    @GetMapping("/play-history")
    public Result<List<Map<String, Object>>> getPlayHistory(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            List<Map<String, Object>> history = userService.getPlayHistory(userId, limit, offset);
            return Result.success(history);
        } catch (Exception e) {
            log.error("获取播放历史失败", e);
            return Result.error("获取失败");
        }
    }

    /**
     * 从Token中提取用户ID
     */
    private String extractUserId(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (token.isEmpty()) {
                return null;
            }
            try {
                return jwtTokenProvider.getUserIdFromToken(token);
            } catch (Exception e) {
                log.warn("Invalid token: {}", e.getMessage());
                return null;
            }
        }
        return null;
    }
}
