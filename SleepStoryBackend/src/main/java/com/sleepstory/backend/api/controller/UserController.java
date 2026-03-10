package com.sleepstory.backend.api.controller;

import com.sleepstory.backend.api.dto.*;
import com.sleepstory.backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取用户睡眠统计
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
     * 从Token中提取用户ID
     */
    private String extractUserId(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return token.isEmpty() ? null : token;
        }
        return null;
    }
}
