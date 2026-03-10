package com.sleepstory.backend.infrastructure.config;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.ClientBuilder;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云短信（融合认证）配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sleepstory.aliyun.sms")
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
     * 端点（融合认证服务）
     */
    private String endpoint = "dypnsapi.aliyuncs.com";

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
     * 验证码长度（4-8位，默认6位）
     */
    private Integer codeLength = 6;

    /**
     * 验证码有效时长（秒，默认900秒=15分钟）
     */
    private Integer validTime = 900;

    /**
     * 发送间隔（秒，默认60秒）
     */
    private Integer interval = 60;

    /**
     * 每天发送次数限制
     */
    private Integer dailyLimit = 10;

    /**
     * 创建阿里云短信Client（融合认证服务）
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
