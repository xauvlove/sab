package com.sleepstory.backend.api.controller;

import com.sleepstory.backend.api.dto.Result;
import com.sleepstory.backend.api.dto.request.FavoriteRequest;
import com.sleepstory.backend.api.dto.response.StoryListResponse;
import com.sleepstory.backend.service.story.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 收藏控制器
 */
@Slf4j
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 切换收藏状态（收藏/取消收藏）
     * @param request 收藏请求（故事ID）
     * @param authHeader Authorization头（Bearer Token）
     * @return 收藏状态和收藏数量
     */
    @PostMapping("/toggle")
    public Result<Map<String, Object>> toggleFavorite(
            @Valid @RequestBody FavoriteRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            boolean isFavorited = favoriteService.toggleFavorite(userId, request.getStoryId());
            int favoriteCount = favoriteService.getFavoriteCount(userId);

            return Result.success(Map.of(
                    "isFavorited", isFavorited,
                    "favoriteCount", favoriteCount
            ));
        } catch (RuntimeException e) {
            log.warn("收藏操作失败: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("收藏操作失败", e);
            return Result.error("操作失败");
        }
    }

    /**
     * 获取用户收藏列表
     * @param authHeader Authorization头（Bearer Token）
     * @return 收藏的故事列表
     */
    @GetMapping("/list")
    public Result<List<StoryListResponse>> getFavorites(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            List<StoryListResponse> favorites = favoriteService.getUserFavorites(userId);
            return Result.success(favorites);
        } catch (Exception e) {
            log.error("获取收藏列表失败", e);
            return Result.error("获取失败");
        }
    }

    /**
     * 检查故事是否已收藏
     * @param storyId 故事ID
     * @param authHeader Authorization头（可选）
     * @return 是否已收藏
     */
    @GetMapping("/check/{storyId}")
    public Result<Map<String, Boolean>> checkFavorite(
            @PathVariable String storyId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.success(Map.of("isFavorited", false));
            }

            boolean isFavorited = favoriteService.isFavorited(userId, storyId);
            return Result.success(Map.of("isFavorited", isFavorited));
        } catch (Exception e) {
            log.error("检查收藏状态失败", e);
            return Result.error("检查失败");
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
