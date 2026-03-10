package com.sleepstory.backend.service.sms;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.CheckSmsVerifyCodeResponse;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.google.common.primitives.Longs;
import com.sleepstory.backend.api.exception.BusinessException;
import com.sleepstory.backend.infrastructure.config.AliyunSmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Integers;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 短信服务（阿里云融合认证）
 * 使用 SendSmsVerifyCode API 发送短信验证码
 * 使用 CheckSmsVerifyCode API 核验短信验证码
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
     * 发送间隔key前缀
     */
    private static final String SMS_INTERVAL_PREFIX = "sms:interval:";

    /**
     * 每天发送次数key前缀
     */
    private static final String SMS_DAILY_COUNT_PREFIX = "sms:count:";

    /**
     * 发送验证码
     * 使用阿里云融合认证的 SendSmsVerifyCode API
     *
     * @param phone 手机号
     * @return 发送是否成功
     */
    public boolean sendVerificationCode(String phone) {
        // 检查发送间隔
        String intervalKey = SMS_INTERVAL_PREFIX + phone;
        Boolean intervalSet = redisTemplate.opsForValue().setIfAbsent(
                intervalKey, "1", smsConfig.getInterval(), TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(intervalSet)) {
            log.warn("验证码发送过于频繁: {}", phone);
            throw new BusinessException(429, "发送过于频繁，请" + smsConfig.getInterval() + "秒后重试");
        }

        // 检查每日发送次数
        String dailyCountKey = SMS_DAILY_COUNT_PREFIX + phone + ":" + getTodayKey();
        Integer dailyCount = (Integer) redisTemplate.opsForValue().get(dailyCountKey);
        if (dailyCount != null && dailyCount >= smsConfig.getDailyLimit()) {
            log.warn("验证码每日发送次数已达上限: {}", phone);
            throw new BusinessException(429, "今日发送次数已达上限，请明天再试");
        }

        try {
            // 构建请求参数
            SendSmsVerifyCodeRequest request = new SendSmsVerifyCodeRequest()
                    .setPhoneNumber(phone)
                    .setSignName(smsConfig.getSignName())
                    .setTemplateCode(smsConfig.getTemplateCode())
                    // 使用占位符，让阿里云自动生成验证码
                    .setTemplateParam("{\"code\":\"##code##\",\"min\":\"" + (smsConfig.getValidTime() / 60) + "\"}")
                    // 设置验证码长度
                    .setCodeLength(smsConfig.getCodeLength().longValue())
                    // 设置验证码有效时长（15分钟=900秒）
                    .setValidTime(smsConfig.getValidTime().longValue())
                    // 设置发送间隔
                    .setInterval(smsConfig.getInterval().longValue())
                    // 设置验证码类型为纯数字
                    .setCodeType(1L)
                    // 不返回验证码（我们通过阿里云API验证）
                    .setReturnVerifyCode(false);

            // 发送短信
            SendSmsVerifyCodeResponse response = smsClient.sendSmsVerifyCode(request);

            if ("OK".equals(response.getBody().getCode())) {
                // 更新每日发送次数
                redisTemplate.opsForValue().increment(dailyCountKey);
                redisTemplate.expire(dailyCountKey, 24 * 60 * 60, TimeUnit.SECONDS);

                log.info("验证码发送成功: {}", phone);
                return true;
            } else {
                log.error("验证码发送失败: {} - {}", phone, response.getBody().getMessage());
                throw new BusinessException(-1, "短信发送失败: " + response.getBody().getMessage());
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("验证码发送异常: {}", phone, e);
            throw new BusinessException("短信发送异常: " + e.getMessage());
        }
    }

    /**
     * 验证验证码
     * 使用阿里云融合认证的 CheckSmsVerifyCode API 进行核销
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 验证是否成功
     */
    public boolean verifyCode(String phone, String code) {
        // 检查是否获取过验证码
        String intervalKey = SMS_INTERVAL_PREFIX + phone;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(intervalKey))) {
            log.warn("未获取过验证码: {}", phone);
            throw new BusinessException(400, "请先获取验证码");
        }

        try {
            // 构建核验请求参数
            CheckSmsVerifyCodeRequest request = new CheckSmsVerifyCodeRequest()
                    .setPhoneNumber(phone)
                    .setVerifyCode(code);

            // 调用阿里云API核验验证码
            CheckSmsVerifyCodeResponse response = smsClient.checkSmsVerifyCode(request);

            if ("OK".equals(response.getBody().getCode())) {
                // 获取核验结果
                String verifyResult = response.getBody().getModel().getVerifyResult();

                if ("PASS".equals(verifyResult)) {
                    log.info("验证码核销成功: {}", phone);
                    // 核销成功后删除发送记录，防止重复使用
                    redisTemplate.delete(intervalKey);
                    return true;
                } else {
                    log.warn("验证码核销失败: {} - 结果: {}", phone, verifyResult);
                    throw new BusinessException(400, "验证码错误");
                }
            } else {
                log.error("验证码核销失败: {} - {}", phone, response.getBody().getMessage());
                throw new BusinessException(400, "验证码验证失败: " + response.getBody().getMessage());
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("验证码核销异常: {}", phone, e);
            throw new BusinessException("验证码验证异常: " + e.getMessage());
        }
    }

    /**
     * 获取今天的日期key
     */
    private String getTodayKey() {
        java.time.LocalDate today = java.time.LocalDate.now();
        return today.toString().replace("-", "");
    }
}
