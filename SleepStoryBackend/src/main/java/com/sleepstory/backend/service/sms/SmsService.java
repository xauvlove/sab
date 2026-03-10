package com.sleepstory.backend.service.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.sleepstory.backend.infrastructure.config.AliyunSmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 短信服务
 * 处理验证码的发送和验证
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    private final Client smsClient;
    private final AliyunSmsConfig smsConfig;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis中验证码的key前缀
     */
    private static final String SMS_CODE_PREFIX = "sms:code:";

    /**
     * 验证码有效期（秒）- 5分钟
     */
    private static final long SMS_CODE_EXPIRE_SECONDS = 300;

    /**
     * 发送验证码间隔（秒）- 60秒
     */
    private static final long SMS_SEND_INTERVAL_SECONDS = 60;

    /**
     * 每天发送次数限制
     */
    private static final int SMS_DAILY_LIMIT = 10;

    /**
     * 每天发送次数key前缀
     */
    private static final String SMS_DAILY_COUNT_PREFIX = "sms:count:";

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @return 发送是否成功
     */
    public boolean sendVerificationCode(String phone) {
        // 检查发送间隔
        String intervalKey = SMS_CODE_PREFIX + phone + ":interval";
        Boolean intervalSet = redisTemplate.opsForValue().setIfAbsent(
                intervalKey, "1", SMS_SEND_INTERVAL_SECONDS, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(intervalSet)) {
            log.warn("验证码发送过于频繁: {}", phone);
            throw new RuntimeException("发送过于频繁，请" + SMS_SEND_INTERVAL_SECONDS + "秒后重试");
        }

        // 检查每日发送次数
        String dailyCountKey = SMS_DAILY_COUNT_PREFIX + phone + ":" + getTodayKey();
        Integer dailyCount = (Integer) redisTemplate.opsForValue().get(dailyCountKey);
        if (dailyCount != null && dailyCount >= SMS_DAILY_LIMIT) {
            log.warn("验证码每日发送次数已达上限: {}", phone);
            throw new RuntimeException("今日发送次数已达上限，请明天再试");
        }

        // 生成6位随机验证码
        String code = generateCode();
        log.info("生成验证码: {} -> {}", phone, code);

        try {
            // 调用阿里云发送短信
            SendSmsRequest request = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName(smsConfig.getSignName())
                    .setTemplateCode(smsConfig.getTemplateCode())
                    .setTemplateParam("{\"code\":\"" + code + "\"}");

            SendSmsResponse response = smsClient.sendSms(request);

            if ("OK".equals(response.getBody().getCode())) {
                // 存储验证码到Redis
                String codeKey = SMS_CODE_PREFIX + phone;
                redisTemplate.opsForValue().set(codeKey, code, SMS_CODE_EXPIRE_SECONDS, TimeUnit.SECONDS);

                // 更新每日发送次数
                redisTemplate.opsForValue().increment(dailyCountKey);
                redisTemplate.expire(dailyCountKey, 24 * 60 * 60, TimeUnit.SECONDS);

                log.info("验证码发送成功: {}", phone);
                return true;
            } else {
                log.error("验证码发送失败: {} - {}", phone, response.getBody().getMessage());
                throw new RuntimeException("短信发送失败: " + response.getBody().getMessage());
            }
        } catch (Exception e) {
            log.error("验证码发送异常: {}", phone, e);
            throw new RuntimeException("短信发送异常: " + e.getMessage());
        }
    }

    /**
     * 验证验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 验证是否成功
     */
    public boolean verifyCode(String phone, String code) {
        String codeKey = SMS_CODE_PREFIX + phone;
        String storedCode = (String) redisTemplate.opsForValue().get(codeKey);

        if (storedCode == null) {
            log.warn("验证码已过期: {}", phone);
            throw new RuntimeException("验证码已过期，请重新获取");
        }

        if (!storedCode.equals(code)) {
            log.warn("验证码错误: {} - 输入: {}, 正确: {}", phone, code, storedCode);
            throw new RuntimeException("验证码错误");
        }

        // 验证成功后删除验证码，防止重复使用
        redisTemplate.delete(codeKey);
        log.info("验证码验证成功: {}", phone);
        return true;
    }

    /**
     * 生成6位随机验证码
     */
    private String generateCode() {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(code);
    }

    /**
     * 获取今天的日期key
     */
    private String getTodayKey() {
        java.time.LocalDate today = java.time.LocalDate.now();
        return today.toString().replace("-", "");
    }
}
