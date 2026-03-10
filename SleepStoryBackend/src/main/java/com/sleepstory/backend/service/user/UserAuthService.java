package com.sleepstory.backend.service.user;

import com.sleepstory.backend.api.dto.response.AuthResponse;
import com.sleepstory.backend.api.dto.request.LoginRequest;
import com.sleepstory.backend.api.dto.request.RegisterRequest;
import com.sleepstory.backend.domain.entity.User;
import com.sleepstory.backend.domain.entity.UserProfile;
import com.sleepstory.backend.domain.repository.UserProfileRepository;
import com.sleepstory.backend.domain.repository.UserRepository;
import com.sleepstory.backend.domain.valueobject.StoryDuration;
import com.sleepstory.backend.domain.valueobject.UserStatus;
import com.sleepstory.backend.infrastructure.security.JwtTokenProvider;
import com.sleepstory.backend.infrastructure.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * 用户认证服务
 * 处理用户注册、登录等认证相关操作
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 用户注册
     * @param request 注册请求（手机号、密码、昵称）
     * @return 认证响应（包含Token和用户信息）
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 检查手机号是否已注册
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("手机号已注册");
        }

        // 创建用户
        User user = User.builder()
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname() != null ? request.getNickname() : generateDefaultNickname())
                .avatarUrl(generateDefaultAvatar())
                .status(UserStatus.ACTIVE)
                .build();

        user = userRepository.save(user);
        log.info("User registered: {}", user.getId());

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

        // 生成JWT Token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getPhone());

        return buildAuthResponse(user, token);
    }

    /**
     * 用户登录
     * @param request 登录请求（手机号、密码）
     * @param clientIp 客户端IP地址
     * @return 认证响应（包含Token和用户信息）
     */
    public AuthResponse login(LoginRequest request, String clientIp) {
        // 查找用户
        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 检查用户状态
        if (!user.isActive()) {
            throw new RuntimeException("用户已被锁定或禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("密码错误");
        }

        // 更新登录信息
        user.recordLogin(clientIp);
        userRepository.save(user);

        log.info("User logged in: {}", user.getId());

        // 生成JWT Token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getPhone());

        return buildAuthResponse(user, token);
    }

    /**
     * 根据Token获取用户信息
     */
    public AuthResponse.UserInfo getUserInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        return AuthResponse.UserInfo.builder()
                .id(user.getId())
                .phone(maskPhone(user.getPhone()))
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * 刷新Token
     */
    public AuthResponse refreshToken(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        String token = jwtTokenProvider.generateToken(user.getId(), user.getPhone());

        return buildAuthResponse(user, token);
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
    private String generateDefaultNickname() {
            return "晚安旅人" + System.currentTimeMillis() % 10000;
    }

    /**
     * 生成默认头像
     */
    private String generateDefaultAvatar() {
        return "https://api.dicebear.com/7.x/avataaars/svg?seed=" + System.currentTimeMillis();
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

    /**
     * 检查手机号是否已注册
     */
    public boolean checkPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * 获取用户信息
     */
    public AuthResponse.UserInfo getUserInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return AuthResponse.UserInfo.builder()
                .id(user.getId())
                .phone(maskPhone(user.getPhone()))
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }

    /**
     * 刷新Token
     */
    public AuthResponse refreshToken(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        String token = jwtTokenProvider.generateToken(user.getId(), user.getPhone());
        return buildAuthResponse(user, token);
    }
}
