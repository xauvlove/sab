package com.sleepstory.backend.domain.service;

import com.sleepstory.backend.domain.entity.Story;
import com.sleepstory.backend.domain.entity.StoryGenerationTask;
import com.sleepstory.backend.domain.valueobject.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StoryGenerationDomainService {

    public StoryGenerationTask createGenerationTask(
            String userId,
            String keywords,
            StoryScene scene,
            StoryMood mood,
            StoryDuration duration) {
        
        return StoryGenerationTask.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .keywords(keywords)
                .scene(scene)
                .mood(mood)
                .duration(duration)
                .status(GenerationStatus.PENDING)
                .progressPercent(0)
                .statusMessage("等待处理...")
                .build();
    }

    public Story createStoryFromGeneration(
            StoryGenerationTask task,
            String title,
            String content,
            String audioUrl) {
        
        return Story.builder()
                .id(UUID.randomUUID().toString())
                .title(title)
                .description(generateDescription(content))
                .category(determineCategory(task.getScene(), task.getMood()))
                .duration(task.getDuration())
                .durationSeconds(task.getDuration().getTargetDurationSeconds())
                .icon(task.getScene().getIcon())
                .gradientColors(generateGradientColors(task.getMood()))
                .audioUrl(audioUrl)
                .isGenerated(true)
                .isFavorite(false)
                .rating(5.0f)
                .playCount(0)
                .content(content)
                .build();
    }

    private String generateDescription(String content) {
        if (content == null || content.isEmpty()) {
            return "AI生成的专属助眠故事";
        }
        String cleanContent = content.replaceAll("\\s+", "");
        if (cleanContent.length() <= 100) {
            return cleanContent;
        }
        return cleanContent.substring(0, 100) + "...";
    }

    private StoryCategory determineCategory(StoryScene scene, StoryMood mood) {
        return switch (scene) {
            case MOONLIGHT, GARDEN -> StoryCategory.NATURE;
            case MOUNTAIN, COTTAGE -> StoryCategory.WARM;
            case BEACH -> StoryCategory.NATURE;
            case JOURNEY -> StoryCategory.FANTASY;
            default -> mood == StoryMood.FANTASY ? StoryCategory.FANTASY : StoryCategory.WARM;
        };
    }

    private String generateGradientColors(StoryMood mood) {
        return switch (mood) {
            case CALM -> "#4ADE80,#14B8A6";
            case WARM -> "#FB923C,#EF4444";
            case FANTASY -> "#A78BFA,#EC4899";
        };
    }

    public String buildGenerationPrompt(
            String keywords,
            StoryScene scene,
            StoryMood mood,
            StoryDuration duration) {
        
        StringBuilder prompt = new StringBuilder();
        prompt.append("请创作一个助眠故事，要求如下：\n\n");
        prompt.append("【场景】").append(scene.getAtmosphere()).append("\n");
        prompt.append("【基调】").append(mood.getDescription()).append("\n");
        prompt.append("【时长】约").append(duration.getDescription()).append("\n");
        prompt.append("【字数】约").append(duration.getTargetWordCount()).append("字\n\n");
        
        if (keywords != null && !keywords.isEmpty()) {
            prompt.append("【关键词】").append(keywords).append("\n\n");
        }
        
        prompt.append("【要求】\n");
        prompt.append("1. 故事要舒缓、平和，适合睡前聆听\n");
        prompt.append("2. 情节简单，不要有激烈的冲突或紧张的情节\n");
        prompt.append("3. 使用优美的描写性语言，营造宁静的氛围\n");
        prompt.append("4. 结尾要温暖、治愈，让人感到安心\n");
        prompt.append("5. 适当使用第二人称，增强代入感\n");
        prompt.append("6. 段落不要太长，便于朗读和聆听\n\n");
        prompt.append("请直接输出故事内容，不需要标题。");
        
        return prompt.toString();
    }
}
