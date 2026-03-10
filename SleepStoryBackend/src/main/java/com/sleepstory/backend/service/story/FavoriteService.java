package com.sleepstory.backend.service.story;

import com.sleepstory.backend.api.response.StoryListResponse;
import com.sleepstory.backend.dal.mapper.FavoriteMapper;
import com.sleepstory.backend.dal.mapper.StoryMapper;
import com.sleepstory.backend.dal.po.StoryPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final StoryMapper storyMapper;

    /**
     * 切换收藏状态
     */
    public boolean toggleFavorite(String userId, String storyId) {
        // 检查故事是否存在
        StoryPO story = storyMapper.selectById(storyId);
        if (story == null) {
            throw new RuntimeException("故事不存在");
        }

        boolean exists = favoriteMapper.exists(userId, storyId);
        if (exists) {
            // 取消收藏
            favoriteMapper.delete(userId, storyId);
            log.info("用户取消收藏: userId={}, storyId={}", userId, storyId);
            return false;
        } else {
            // 添加收藏
            favoriteMapper.insert(userId, storyId);
            log.info("用户添加收藏: userId={}, storyId={}", userId, storyId);
            return true;
        }
    }

    /**
     * 检查是否已收藏
     */
    public boolean isFavorited(String userId, String storyId) {
        return favoriteMapper.exists(userId, storyId);
    }

    /**
     * 获取用户收藏列表
     */
    public List<StoryListResponse> getUserFavorites(String userId) {
        List<StoryPO> stories = favoriteMapper.selectByUserId(userId);
        return stories.stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户收藏数量
     */
    public int getFavoriteCount(String userId) {
        return favoriteMapper.countByUserId(userId);
    }

    /**
     * 转换为列表响应
     */
    private StoryListResponse convertToListResponse(StoryPO story) {
        return StoryListResponse.builder()
                .id(story.getId())
                .title(story.getTitle())
                .description(story.getDescription())
                .category(story.getCategory())
                .duration(story.getDuration())
                .icon(story.getIcon())
                .gradientColors(story.getGradientColors())
                .rating(story.getRating())
                .playCount(story.getPlayCount())
                .build();
    }
}
