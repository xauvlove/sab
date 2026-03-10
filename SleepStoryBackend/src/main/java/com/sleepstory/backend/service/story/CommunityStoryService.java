package com.sleepstory.backend.service.story;

import com.sleepstory.backend.api.request.PublishStoryRequest;
import com.sleepstory.backend.api.response.CommunityStoryDetailResponse;
import com.sleepstory.backend.api.response.CommunityStoryResponse;
import com.sleepstory.backend.dal.mapper.CommunityStoryMapper;
import com.sleepstory.backend.domain.entity.CommunityStory;
import com.sleepstory.backend.domain.entity.UserProfile;
import com.sleepstory.backend.dal.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 他人创作故事服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityStoryService {

    private final CommunityStoryMapper communityStoryMapper;
    private final UserProfileMapper userProfileMapper;
    // 假设我们有一个文件存储服务，这里只是示意
    // private final FileStorageService fileStorageService;

    /**
     * 发布故事
     */
    @Transactional
    public CommunityStory publishStory(String userId, PublishStoryRequest request) {
        // 获取用户信息
        UserProfile userProfile = userProfileMapper.selectByUserId(userId);

        // 创建故事实体
        CommunityStory story = CommunityStory.builder()
                .userId(userId)
                .title(request.getTitle())
                .summary(request.getSummary())
                .content(request.getContent())
                .voiceConfig(request.getVoiceConfig())
                .category(request.getCategory())
                .tags(request.getTags())
                .status(1) // 已发布
                .likesCount(0)
                .playsCount(0)
                .userNickname(userProfile != null ? userProfile.getNickname() : "匿名用户")
                .userAvatar(userProfile != null ? userProfile.getAvatarUrl() : null)
                // 添加音频文件URL字段（假设有音频上传）
                .audioUrl(generateAudioUrl(request.getTitle()))
                .build();

        communityStoryMapper.insert(story);
        log.info("Story published: id={}, userId={}, title={}", story.getId(), userId, story.getTitle());

        return story;
    }

    // 生成音频文件URL的辅助方法（示例实现）
    private String generateAudioUrl(String title) {
        // 这只是一个示例实现，实际应该集成真正的文件存储服务
        // 比如上传到AWS S3、阿里云OSS等
        return "https://example.com/audio/" + UUID.randomUUID().toString() + "_" + title.replaceAll("[^a-zA-Z0-9]", "_") + ".mp3";
    }

    /**
     * 获取故事列表（Feed流）
     */
    public List<CommunityStoryResponse> getStoryList(String category, String orderBy, int page, int size, String currentUserId) {
        int offset = (page - 1) * size;
        List<Map<String, Object>> rawList = communityStoryMapper.selectList(1, category, orderBy, offset, size);

        List<CommunityStoryResponse> result = new ArrayList<>();
        for (Map<String, Object> item : rawList) {
            CommunityStoryResponse response = CommunityStoryResponse.builder()
                    .id(((Number) item.get("id")).longValue())
                    .title((String) item.get("title"))
                    .summary((String) item.get("summary"))
                    .category((String) item.get("category"))
                    .tags((String) item.get("tags"))
                    .likesCount(((Number) item.get("likesCount")).intValue())
                    .playsCount(((Number) item.get("playsCount")).intValue())
                    .userNickname((String) item.get("userNickname"))
                    .userAvatar((String) item.get("userAvatar"))
                    .createdAt(item.get("createdAt") != null ? 
                        ((java.sql.Timestamp) item.get("createdAt")).toLocalDateTime() : null)
                    .isLiked(false)
                    .build();

            // 检查是否已点赞
            if (currentUserId != null) {
                Integer liked = communityStoryMapper.checkLiked(currentUserId, response.getId());
                response.setIsLiked(liked != null && liked > 0);
            }

            result.add(response);
        }

        return result;
    }

    /**
     * 获取故事详情
     */
    public CommunityStoryDetailResponse getStoryDetail(Long storyId, String currentUserId) {
        CommunityStory story = communityStoryMapper.selectById(storyId);
        if (story == null) {
            throw new RuntimeException("故事不存在");
        }

        // 增加播放次数
        communityStoryMapper.incrementPlaysCount(storyId);

        CommunityStoryDetailResponse response = CommunityStoryDetailResponse.builder()
                .id(story.getId())
                .title(story.getTitle())
                .summary(story.getSummary())
                .content(story.getContent())
                .voiceConfig(story.getVoiceConfig())
                .category(story.getCategory())
                .tags(story.getTags())
                .likesCount(story.getLikesCount())
                .playsCount(story.getPlaysCount())
                .userId(story.getUserId())
                .userNickname(story.getUserNickname())
                .userAvatar(story.getUserAvatar())
                .createdAt(story.getCreatedAt())
                .isOwner(false)
                .isLiked(false)
                .build();

        // 判断是否为自己的故事
        if (currentUserId != null && currentUserId.equals(story.getUserId())) {
            response.setIsOwner(true);
        }

        // 检查是否已点赞
        if (currentUserId != null) {
            Integer liked = communityStoryMapper.checkLiked(currentUserId, storyId);
            response.setIsLiked(liked != null && liked > 0);
        }

        return response;
    }

    /**
     * 切换点赞状态
     */
    @Transactional
    public boolean toggleLike(String userId, Long storyId) {
        // 检查故事是否存在
        CommunityStory story = communityStoryMapper.selectById(storyId);
        if (story == null) {
            throw new RuntimeException("故事不存在");
        }

        // 检查是否已点赞
        Integer liked = communityStoryMapper.checkLiked(userId, storyId);

        if (liked != null && liked > 0) {
            // 取消点赞
            communityStoryMapper.deleteLike(userId, storyId);
            communityStoryMapper.decrementLikesCount(storyId);
            log.info("Story unliked: userId={}, storyId={}", userId, storyId);
            return false;
        } else {
            // 点赞
            communityStoryMapper.insertLike(userId, storyId);
            communityStoryMapper.incrementLikesCount(storyId);
            log.info("Story liked: userId={}, storyId={}", userId, storyId);
            return true;
        }
    }

    /**
     * 获取用户发布的故事列表
     */
    public List<CommunityStoryResponse> getUserStories(String userId, int page, int size) {
        int offset = (page - 1) * size;
        List<Map<String, Object>> rawList = communityStoryMapper.selectByUserId(userId, offset, size);

        List<CommunityStoryResponse> result = new ArrayList<>();
        for (Map<String, Object> item : rawList) {
            CommunityStoryResponse response = CommunityStoryResponse.builder()
                    .id(((Number) item.get("id")).longValue())
                    .title((String) item.get("title"))
                    .summary((String) item.get("summary"))
                    .category((String) item.get("category"))
                    .tags((String) item.get("tags"))
                    .likesCount(((Number) item.get("likesCount")).intValue())
                    .playsCount(((Number) item.get("playsCount")).intValue())
                    .userNickname((String) item.get("userNickname"))
                    .userAvatar((String) item.get("userAvatar"))
                    .createdAt(item.get("createdAt") != null ? 
                        ((java.sql.Timestamp) item.get("createdAt")).toLocalDateTime() : null)
                    .isLiked(true)
                    .build();
            result.add(response);
        }

        return result;
    }

    /**
     * 删除故事
     */
    @Transactional
    public void deleteStory(String userId, Long storyId) {
        CommunityStory story = communityStoryMapper.selectById(storyId);
        if (story == null) {
            throw new RuntimeException("故事不存在");
        }

        if (!story.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此故事");
        }

        communityStoryMapper.deleteById(storyId);
        log.info("Story deleted: userId={}, storyId={}", userId, storyId);
    }
}