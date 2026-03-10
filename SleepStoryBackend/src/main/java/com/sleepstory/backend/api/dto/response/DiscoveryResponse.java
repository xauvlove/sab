package com.sleepstory.backend.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发现页数据响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscoveryResponse {

    /**
     * 热门标签
     */
    private List<TagResponse> hotTags;

    /**
     * 分类列表
     */
    private List<CategoryResponse> categories;

    /**
     * 排行榜故事
     */
    private List<StoryListResponse> rankings;

    /**
     * 标签响应
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagResponse {

        /**
         * 标签ID
         */
        private Integer id;

        /**
         * 标签名称
         */
        private String name;

        /**
         * 引用次数
         */
        private Integer usageCount;
    }

    /**
     * 分类响应
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryResponse {

        /**
         * 分类名称
         */
        private String name;

        /**
         * 图标
         */
        private String icon;

        /**
         * 故事数量
         */
        private Integer storyCount;
    }
}
