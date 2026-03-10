package com.sleepstory.backend.api.controller;

import com.sleepstory.backend.api.dto.*;
import com.sleepstory.backend.service.story.DiscoveryService;
import com.sleepstory.backend.service.story.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 发现页控制器
 */
@Slf4j
@RestController
@RequestMapping("/discovery")
@RequiredArgsConstructor
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    /**
     * 获取发现页数据
     */
    @GetMapping
    public Result<DiscoveryResponse> getDiscoveryData(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            DiscoveryResponse data = discoveryService.getDiscoveryData(userId);
            return Result.success(data);
        } catch (Exception e) {
            log.error("获取发现页数据失败", e);
            return Result.error("获取数据失败");
        }
    }

    /**
     * 搜索故事
     */
    @GetMapping("/search")
    public Result<List<StoryListResponse>> search(
            @RequestParam String keyword,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            List<StoryListResponse> stories = discoveryService.searchStories(keyword);
            return Result.success(stories);
        } catch (Exception e) {
            log.error("搜索失败: {}", e.getMessage());
            return Result.error("搜索失败");
        }
    }

    /**
     * 获取分类下的故事
     */
    @GetMapping("/category/{category}")
    public Result<List<StoryListResponse>> getByCategory(
            @PathVariable String category,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            List<StoryListResponse> stories = discoveryService.getStoriesByCategory(category.toUpperCase());
            return Result.success(stories);
        } catch (Exception e) {
            log.error("获取分类故事失败: {}", e.getMessage());
            return Result.error("获取失败");
        }
    }

    /**
     * 获取故事详情
     */
    @GetMapping("/story/{storyId}")
    public Result<StoryDetailResponse> getStoryDetail(
            @PathVariable String storyId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            StoryDetailResponse story = discoveryService.getStoryDetail(storyId, userId);
            return Result.success(story);
        } catch (RuntimeException e) {
            log.warn("获取故事详情失败: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("获取故事详情失败", e);
            return Result.error("获取详情失败");
        }
    }

    /**
     * 从Token中提取用户ID
     */
    private String extractUserId(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // 实际应该解析JWT
            return token.isEmpty() ? null : token;
        }
        return null;
    }
}
