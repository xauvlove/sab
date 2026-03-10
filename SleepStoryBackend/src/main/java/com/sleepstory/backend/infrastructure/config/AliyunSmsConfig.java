package com.sleepstory.backend.infrastructure.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.ClientBuilder;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云短信配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.aliyun.sms")
public class AliyunSmsConfig {

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * 端点
     */
    private String endpoint = "dysmsapi.aliyuncs.com";

    /**
     * 区域ID
     */
    private String regionId = "cn-hangzhou";

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板Code
     */
    private String templateCode;

    /**
     * 创建阿里云短信Client
     */
    @Bean
    public Client smsClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(endpoint)
                .setRegionId(regionId);
        return ClientBuilder.build(config);
    }
}
