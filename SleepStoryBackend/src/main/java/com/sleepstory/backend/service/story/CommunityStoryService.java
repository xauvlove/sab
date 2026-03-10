package com.sleepstory.backend.service.story;

import com.sleepstory.backend.api.request.PublishStoryRequest;
import com.sleepstory.backend.api.response.CommunityStoryDetailResponse;
import com.sleepstory.backend.api.response.CommunityStoryResponse;
import com.sleepstory.backend.domain.entity.CommunityStory;
import com.sleepstory.backend.domain.entity.User;
import com.sleepstory.backend.domain.entity.UserProfile;
import com.sleepstory.backend.domain.repository.CommunityStoryRepository;
import com.sleepstory.backend.domain.repository.UserProfileRepository;
import com.sleepstory.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 他人创作故事服务
 * 遵循DDD架构：Service层通过Repository接口操作数据
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityStoryService {

    private final CommunityStoryRepository communityStoryRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    /**
     * 发布故事
     */
    @Transactional
    public CommunityStory publishStory(String userId, PublishStoryRequest request) {
        // 通过Repository获取用户信息
        Optional<UserProfile> userProfileOpt = userProfileRepository.findByUserId(userId);
        Optional<User> userOpt = userRepository.findById(userId);
        
        String nickname = "匿名用户";
        String avatarUrl = null;
        
        // 优先使用User的昵称和头像
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            nickname = user.getNickname() != null ? user.getNickname() : "匿名用户";
            avatarUrl = user.getAvatarUrl();
        }
        
        // 如果User没有头像，尝试使用UserProfile的头像
        if (avatarUrl == null && userProfileOpt.isPresent()) {
            avatarUrl = userProfileOpt.get().getAvatarUrl();
        }

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
                .userNickname(nickname)
                .userAvatar(avatarUrl)
                .build();

        // 通过Repository保存
        CommunityStory savedStory = communityStoryRepository.save(story);
        log.info("Story published: id={}, userId={}, title={}", savedStory.getId(), userId, savedStory.getTitle());

        return savedStory;
    }

    /**
     * 获取故事列表（Feed流）
     */
    public List<CommunityStoryResponse> getStoryList(String category, String orderBy, int page, int size, String currentUserId) {
        int offset = (page - 1) * size;
        
        // 通过Repository查询列表
        List<CommunityStory> stories = communityStoryRepository.findList(1, category, orderBy, offset, size);

        List<CommunityStoryResponse> result = new ArrayList<>();
        for (CommunityStory story : stories) {
            CommunityStoryResponse response = CommunityStoryResponse.builder()
                    .id(story.getId())
                    .title(story.getTitle())
                    .summary(story.getSummary())
                    .category(story.getCategory())
                    .tags(story.getTags())
                    .likesCount(story.getLikesCount())
                    .playsCount(story.getPlaysCount())
                    .userNickname(story.getUserNickname())
                    .userAvatar(story.getUserAvatar())
                    .createdAt(story.getCreatedAt())
                    .isLiked(false)
                    .build();

            // 检查是否已点赞
            if (currentUserId != null) {
                boolean liked = communityStoryRepository.checkLiked(currentUserId, story.getId());
                response.setIsLiked(liked);
            }

            result.add(response);
        }

        return result;
    }

    /**
     * 获取故事详情
     */
    public CommunityStoryDetailResponse getStoryDetail(Long storyId, String currentUserId) {
        // 通过Repository查询
        Optional<CommunityStory> storyOpt = communityStoryRepository.findById(storyId);
        if (storyOpt.isEmpty()) {
            throw new RuntimeException("故事不存在");
        }

        CommunityStory story = storyOpt.get();

        // 增加播放次数
        communityStoryRepository.incrementPlaysCount(storyId);

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
            boolean liked = communityStoryRepository.checkLiked(currentUserId, storyId);
            response.setIsLiked(liked);
        }

        return response;
    }

    /**
     * 切换点赞状态
     */
    @Transactional
    public boolean toggleLike(String userId, Long storyId) {
        // 通过Repository检查故事是否存在
        Optional<CommunityStory> storyOpt = communityStoryRepository.findById(storyId);
        if (storyOpt.isEmpty()) {
            throw new RuntimeException("故事不存在");
        }

        // 检查是否已点赞
        boolean alreadyLiked = communityStoryRepository.checkLiked(userId, storyId);

        if (alreadyLiked) {
            // 取消点赞
            communityStoryRepository.deleteLike(userId, storyId);
            communityStoryRepository.decrementLikesCount(storyId);
            log.info("Story unliked: userId={}, storyId={}", userId, storyId);
            return false;
        } else {
            // 点赞
            communityStoryRepository.insertLike(userId, storyId);
            communityStoryRepository.incrementLikesCount(storyId);
            log.info("Story liked: userId={}, storyId={}", userId, storyId);
            return true;
        }
    }

    /**
     * 获取用户发布的故事列表
     */
    public List<CommunityStoryResponse> getUserStories(String userId, int page, int size) {
        int offset = (page - 1) * size;
        
        // 通过Repository查询
        List<CommunityStory> stories = communityStoryRepository.findByUserId(userId, offset, size);

        List<CommunityStoryResponse> result = new ArrayList<>();
        for (CommunityStory story : stories) {
            CommunityStoryResponse response = CommunityStoryResponse.builder()
                    .id(story.getId())
                    .title(story.getTitle())
                    .summary(story.getSummary())
                    .category(story.getCategory())
                    .tags(story.getTags())
                    .likesCount(story.getLikesCount())
                    .playsCount(story.getPlaysCount())
                    .userNickname(story.getUserNickname())
                    .userAvatar(story.getUserAvatar())
                    .createdAt(story.getCreatedAt())
                    .isLiked(true) // 自己的故事默认已点赞
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
        // 通过Repository查询
        Optional<CommunityStory> storyOpt = communityStoryRepository.findById(storyId);
        if (storyOpt.isEmpty()) {
            throw new RuntimeException("故事不存在");
        }

        CommunityStory story = storyOpt.get();

        if (!story.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此故事");
        }

        communityStoryRepository.deleteById(storyId);
        log.info("Story deleted: userId={}, storyId={}", userId, storyId);
    }
}
