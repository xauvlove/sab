package com.sleepstory.backend.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 发布故事请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishStoryRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;

    @Size(max = 500, message = "简介长度不能超过500个字符")
    private String summary;

    @NotBlank(message = "内容不能为空")
    @Size(max = 20000, message = "内容长度不能超过20000个字符")
    private String content;

    /**
     * 语音配置JSON
     */
    private String voiceConfig;

    @Size(max = 50, message = "分类名称过长")
    private String category;

    @Size(max = 200, message = "标签总长度不能超过200个字符")
    private String tags;
}
