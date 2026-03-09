package com.sleepstory.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 眠语后端服务启动类
 * AI助眠故事生成服务
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SleepStoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SleepStoryApplication.class, args);
    }
}
