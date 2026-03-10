package com.sleepstory.backend.service.story;

import com.sleepstory.backend.api.dto.*;
import com.sleepstory.backend.dal.mapper.FavoriteMapper;
import com.sleepstory.backend.dal.mapper.StoryMapper;
import com.sleepstory.backend.dal.po.StoryPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发现页服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final StoryMapper storyMapper;
    private final FavoriteMapper favoriteMapper;

    /**
     * 获取发现页数据
     */
    public DiscoveryResponse getDiscoveryData(String userId) {
        // 获取热门标签
        List<DiscoveryResponse.TagResponse> hotTags = getHotTags();

        // 获取分类列表
        List<DiscoveryResponse.CategoryResponse> categories = getCategories();

        // 获取排行榜
        List<StoryListResponse> rankings = getRankings();

        return DiscoveryResponse.builder()
                .hotTags(hotTags)
                .categories(categories)
                .rankings(rankings)
                .build();
    }

    /**
     * 获取热门标签
     */
    private List<DiscoveryResponse.TagResponse> getHotTags() {
        // 模拟热门标签数据
        List<DiscoveryResponse.TagResponse> tags = new ArrayList<>();
        tags.add(DiscoveryResponse.TagResponse.builder().id(1).name("雨声助眠").usageCount(1280).build());
        tags.add(DiscoveryResponse.TagResponse.builder().id(2).name("森林漫步").usageCount(980).build());
        tags.add(DiscoveryResponse.TagResponse.builder().id(3).name("星空幻想").usageCount(856).build());
        tags.add(DiscoveryResponse.TagResponse.builder().id(4).name("海浪轻拍").usageCount(742).build());
        tags.add(DiscoveryResponse.TagResponse.builder().id(5).name("冥想引导").usageCount(621).build());
        tags.add(DiscoveryResponse.TagResponse.builder().id(6).name("古风故事").usageCount(534).build());
        return tags;
    }

    /**
     * 获取分类列表
     */
    private List<DiscoveryResponse.CategoryResponse> getCategories() {
        List<DiscoveryResponse.CategoryResponse> categories = new ArrayList<>();
        categories.add(DiscoveryResponse.CategoryResponse.builder().name("自然之声").icon("🌲").storyCount(128).build());
        categories.add(DiscoveryResponse.CategoryResponse.builder().name("奇幻世界").icon("🏰").storyCount(96).build());
        categories.add(DiscoveryResponse.CategoryResponse.builder().name("冥想疗愈").icon("🧘").storyCount(84).build());
        categories.add(DiscoveryResponse.CategoryResponse.builder().name("经典文学").icon("📚").storyCount(156).build());
        return categories;
    }

    /**
     * 获取排行榜
     */
    private List<StoryListResponse> getRankings() {
        List<StoryPO> topStories = storyMapper.selectTopStories(10);
        return topStories.stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());
    }

    /**
     * 搜索故事
     */
    public List<StoryListResponse> searchStories(String keyword) {
        List<StoryPO> stories = storyMapper.searchByKeyword(keyword);
        return stories.stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据分类获取故事列表
     */
    public List<StoryListResponse> getStoriesByCategory(String category) {
        List<StoryPO> stories = storyMapper.selectByCategory(category);
        return stories.stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取故事详情
     */
    public StoryDetailResponse getStoryDetail(String storyId, String userId) {
        StoryPO story = storyMapper.selectById(storyId);
        if (story == null) {
            throw new RuntimeException("故事不存在");
        }

        boolean isFavorited = false;
        if (userId != null) {
            isFavorited = favoriteMapper.exists(userId, storyId);
        }

        return StoryDetailResponse.builder()
                .id(story.getId())
                .title(story.getTitle())
                .description(story.getDescription())
                .category(story.getCategory())
                .duration(story.getDuration())
                .durationSeconds(story.getDurationSeconds())
                .icon(story.getIcon())
                .gradientColors(story.getGradientColors())
                .audioUrl(story.getAudioUrl())
                .rating(story.getRating())
                .playCount(story.getPlayCount())
                .content(story.getContent())
                .isFavorited(isFavorited)
                .tags(getStoryTags(story.getCategory()))
                .build();
    }

    /**
     * 获取故事标签
     */
    private List<String> getStoryTags(String category) {
        List<String> tags = new ArrayList<>();
        switch (category) {
            case "NATURE":
                tags.add("自然");
                tags.add("放松");
                break;
            case "FANTASY":
                tags.add("奇幻");
                tags.add("冒险");
                break;
            case "MEDITATION":
                tags.add("冥想");
                tags.add("治愈");
                break;
            default:
                tags.add("睡前故事");
        }
        return tags;
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
