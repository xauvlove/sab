package com.sleepstory.backend.api.controller;

import com.sleepstory.backend.api.dto.Result;
import com.sleepstory.backend.api.request.PublishStoryRequest;
import com.sleepstory.backend.api.response.CommunityStoryDetailResponse;
import com.sleepstory.backend.api.response.CommunityStoryResponse;
import com.sleepstory.backend.infrastructure.security.JwtTokenProvider;
import com.sleepstory.backend.service.story.CommunityStoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 他人创作故事控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class CommunityStoryController {

    private final CommunityStoryService communityStoryService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 发布故事
     * @param request 发布请求
     * @param authHeader Authorization头
     * @return 发布的故事信息
     */
    @PostMapping("/publish")
    public Result<CommunityStoryResponse> publishStory(
            @Valid @RequestBody PublishStoryRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            var story = communityStoryService.publishStory(userId, request);

            CommunityStoryResponse response = CommunityStoryResponse.builder()
                    .id(story.getId())
                    .title(story.getTitle())
                    .summary(story.getSummary())
                    .category(story.getCategory())
                    .tags(story.getTags())
                    .likesCount(0)
                    .playsCount(0)
                    .userNickname(story.getUserNickname())
                    .userAvatar(story.getUserAvatar())
                    .createdAt(story.getCreatedAt())
                    .isLiked(false)
                    .build();

            return Result.success(response);
        } catch (RuntimeException e) {
            log.warn("发布故事失败: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("发布故事错误", e);
            return Result.error("发布失败，请稍后重试");
        }
    }

    /**
     * 获取故事列表（Feed流）
     * @param category 分类筛选
     * @param orderBy 排序方式：latest/hot
     * @param page 页码
     * @param size 每页数量
     * @param authHeader Authorization头
     * @return 故事列表
     */
    @GetMapping("/feed")
    public Result<List<CommunityStoryResponse>> getStoryFeed(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "latest") String orderBy,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            List<CommunityStoryResponse> stories = communityStoryService.getStoryList(
                    category, orderBy, page, size, userId);
            return Result.success(stories);
        } catch (Exception e) {
            log.error("获取故事列表失败", e);
            return Result.error("获取失败");
        }
    }

    /**
     * 获取故事详情
     * @param storyId 故事ID
     * @param authHeader Authorization头
     * @return 故事详情
     */
    @GetMapping("/{storyId}")
    public Result<CommunityStoryDetailResponse> getStoryDetail(
            @PathVariable Long storyId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            CommunityStoryDetailResponse story = communityStoryService.getStoryDetail(storyId, userId);
            return Result.success(story);
        } catch (RuntimeException e) {
            log.warn("获取故事详情失败: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("获取故事详情错误", e);
            return Result.error("获取失败");
        }
    }

    /**
     * 点赞/取消点赞
     * @param storyId 故事ID
     * @param authHeader Authorization头
     * @return 点赞状态
     */
    @PostMapping("/{storyId}/like")
    public Result<Map<String, Object>> toggleLike(
            @PathVariable Long storyId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            boolean isLiked = communityStoryService.toggleLike(userId, storyId);
            return Result.success(Map.of("isLiked", isLiked));
        } catch (RuntimeException e) {
            log.warn("点赞操作失败: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("点赞操作错误", e);
            return Result.error("操作失败");
        }
    }

    /**
     * 获取用户发布的故事列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @return 故事列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<CommunityStoryResponse>> getUserStories(
            @PathVariable String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<CommunityStoryResponse> stories = communityStoryService.getUserStories(userId, page, size);
            return Result.success(stories);
        } catch (Exception e) {
            log.error("获取用户故事失败", e);
            return Result.error("获取失败");
        }
    }

    /**
     * 删除故事
     * @param storyId 故事ID
     * @param authHeader Authorization头
     * @return 操作结果
     */
    @DeleteMapping("/{storyId}")
    public Result<Void> deleteStory(
            @PathVariable Long storyId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = extractUserId(authHeader);
            if (userId == null) {
                return Result.unauthorized("请先登录");
            }

            communityStoryService.deleteStory(userId, storyId);
            return Result.success(null);
        } catch (RuntimeException e) {
            log.warn("删除故事失败: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("删除故事错误", e);
            return Result.error("删除失败");
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
