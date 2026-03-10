package com.sleepstory.backend.service.sms;

import com.sleepstory.backend.api.response.AuthResponse;
import com.sleepstory.backend.api.request.SmsLoginRequest;
import com.sleepstory.backend.domain.entity.User;
import com.sleepstory.backend.domain.entity.UserProfile;
import com.sleepstory.backend.domain.repository.UserProfileRepository;
import com.sleepstory.backend.domain.repository.UserRepository;
import com.sleepstory.backend.domain.valueobject.StoryDuration;
import com.sleepstory.backend.domain.valueobject.UserStatus;
import com.sleepstory.backend.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * 短信验证码认证服务
 * 处理验证码登录和注册
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsAuthService {

    private final SmsService smsService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 验证码登录/注册
     * 如果用户不存在则自动注册，如果已存在则直接登录
     *
     * @param request  验证码登录请求
     * @param clientIp 客户端IP
     * @return 认证响应（包含Token和用户信息）
     */
    @Transactional
    public AuthResponse loginOrRegister(SmsLoginRequest request, String clientIp) {
        // 验证验证码
        smsService.verifyCode(request.getPhone(), request.getCode());

        // 查询用户是否存在
        User user = userRepository.findByPhone(request.getPhone()).orElse(null);

        if (user == null) {
            // 新用户 - 自动注册
            log.info("新用户注册: {}", request.getPhone());
            user = createNewUser(request.getPhone());
        } else {
            // 老用户 - 检查状态
            if (!user.isActive()) {
                throw new RuntimeException("用户已被锁定或禁用");
            }
            // 更新登录信息
            user.recordLogin(clientIp);
            userRepository.save(user);
            log.info("用户登录: {}", user.getId());
        }

        // 生成JWT Token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getPhone());
        return buildAuthResponse(user, token);
    }

    /**
     * 创建新用户
     */
    private User createNewUser(String phone) {
        // 创建用户
        User user = User.builder()
                .phone(phone)
                .passwordHash("")  // 验证码登录不需要密码
                .nickname(generateDefaultNickname(phone))
                .avatarUrl(generateDefaultAvatar(phone))
                .status(UserStatus.ACTIVE)
                .build();
        user = userRepository.save(user);

        // 创建用户资料
        UserProfile profile = UserProfile.builder()
                .userId(user.getId())
                .sleepTime("22:30")
                .preferredDuration(StoryDuration.MEDIUM)
                .preferredCategories(new ArrayList<>())
                .streakDays(0)
                .totalStoriesListened(0)
                .totalListeningMinutes(0)
                .build();
        userProfileRepository.save(profile);

        log.info("新用户创建成功: {}", user.getId());
        return user;
    }

    /**
     * 构建认证响应
     */
    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .phone(maskPhone(user.getPhone()))
                        .nickname(user.getNickname())
                        .avatarUrl(user.getAvatarUrl())
                        .createdAt(user.getCreatedAt())
                        .build())
                .build();
    }

    /**
     * 生成默认昵称
     */
    private String generateDefaultNickname(String phone) {
        return "晚安旅人" + phone.substring(7);
    }

    /**
     * 生成默认头像
     */
    private String generateDefaultAvatar(String phone) {
        return "https://api.dicebear.com/7.x/avataaars/svg?seed=" + phone;
    }

    /**
     * 手机号脱敏
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
