package com.sleepstory.backend.api.controller;

import com.sleepstory.backend.api.dto.Result;
import com.sleepstory.backend.api.request.LoginRequest;
import com.sleepstory.backend.api.request.RegisterRequest;
import com.sleepstory.backend.api.request.SendCodeRequest;
import com.sleepstory.backend.api.request.SmsLoginRequest;
import com.sleepstory.backend.api.response.AuthResponse;
import com.sleepstory.backend.infrastructure.security.JwtTokenProvider;
import com.sleepstory.backend.service.sms.SmsAuthService;
import com.sleepstory.backend.service.sms.SmsService;
import com.sleepstory.backend.service.user.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 处理用户注册、登录等认证相关请求
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthService userAuthService;
    private final SmsService smsService;
    private final SmsAuthService smsAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 用户注册
     * @param request 注册请求（包含手机号、密码、可选昵称）
     * @return 认证响应（Token和用户信息）
     */
    @PostMapping("/register")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            log.info("Register request for phone: {}", request.getPhone());
            AuthResponse response = userAuthService.register(request);
            return Result.success(response);
        } catch (RuntimeException e) {
            log.warn("Register failed: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Register error: {}", e.getMessage(), e);
            return Result.error("注册失败，请稍后重试");
        }
    }

    /**
     * 用户登录
     * @param request 登录请求（手机号和密码）
     * @param httpRequest HTTP请求（用于获取客户端IP）
     * @return 认证响应（Token和用户信息）
     */
    @PostMapping("/login")
    public Result<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        try {
            log.info("Login request for phone: {}", request.getPhone());
            String clientIp = getClientIp(httpRequest);
            AuthResponse response = userAuthService.login(request, clientIp);
            return Result.success(response);
        } catch (RuntimeException e) {
            log.warn("Login failed: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage(), e);
            return Result.error("登录失败，请稍后重试");
        }
    }

    /**
     * 发送验证码
     * @param request 发送请求（包含手机号）
     * @return 操作结果
     */
    @PostMapping("/sms/send")
    public Result<Void> sendVerificationCode(
            @Valid @RequestBody SendCodeRequest request) {
        try {
            log.info("Send SMS code request for phone: {}", request.getPhone());
            smsService.sendVerificationCode(request.getPhone());
            return Result.success(null);
        } catch (RuntimeException e) {
            log.warn("Send SMS failed: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Send SMS error: {}", e.getMessage(), e);
            return Result.error("发送验证码失败，请稍后重试");
        }
    }

    /**
     * 验证码登录/注册
     * @param request 登录请求（手机号和验证码）
     * @param httpRequest HTTP请求（用于获取客户端IP）
     * @return 认证响应（Token和用户信息）
     */
    @PostMapping("/sms/login")
    public Result<AuthResponse> smsLogin(
            @Valid @RequestBody SmsLoginRequest request,
            HttpServletRequest httpRequest) {
        try {
            log.info("SMS login request for phone: {}", request.getPhone());
            String clientIp = getClientIp(httpRequest);
            AuthResponse response = smsAuthService.loginOrRegister(request, clientIp);
            return Result.success(response);
        } catch (RuntimeException e) {
            log.warn("SMS login failed: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("SMS login error: {}", e.getMessage(), e);
            return Result.error("登录失败，请稍后重试");
        }
    }

    /**
     * 获取当前用户信息
     * @param authHeader Authorization头（Bearer Token）
     * @return 用户基本信息
     */
    @GetMapping("/me")
    public Result<AuthResponse.UserInfo> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.unauthorized("未提供有效的认证信息");
            }

            String token = authHeader.substring(7);
            String userId = jwtTokenProvider.getUserIdFromToken(token);

            AuthResponse.UserInfo userInfo = userAuthService.getUserInfo(userId);
            return Result.success(userInfo);
        } catch (RuntimeException e) {
            log.warn("Get user info failed: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Get user info error: {}", e.getMessage(), e);
            return Result.error("获取用户信息失败");
        }
    }

    /**
     * 刷新Token
     * @param authHeader Authorization头（Bearer Token）
     * @return 新的认证响应
     */
    @PostMapping("/refresh")
    public Result<AuthResponse> refreshToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.unauthorized("未提供有效的认证信息");
            }
            String token = authHeader.substring(7);
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            AuthResponse response = userAuthService.refreshToken(userId);
            return Result.success(response);
        } catch (RuntimeException e) {
            log.warn("Refresh token failed: {}", e.getMessage());
            return Result.badRequest(e.getMessage());
        } catch (Exception e) {
            log.error("Refresh token error: {}", e.getMessage(), e);
            return Result.error("刷新令牌失败");
        }
    }

    /**
     * 检查手机号是否已注册
     * @param phone 手机号
     * @return 是否已注册（true=已注册，false=未注册）
     */
    @GetMapping("/check-phone")
    public Result<Boolean> checkPhone(@RequestParam String phone) {
        try {
            boolean exists = userAuthService.checkPhone(phone);
            return Result.success(exists);
        } catch (Exception e) {
            log.error("Check phone error: {}", e.getMessage(), e);
            return Result.error("检查失败");
        }
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            // 获取第一个非unknown的IP地址，防止IP欺骗
            int idx = xForwardedFor.indexOf(",");
            if (idx != -1) {
                xForwardedFor = xForwardedFor.substring(0, idx);
            }
            xForwardedFor = xForwardedFor.trim();
            if (!"unknown".equalsIgnoreCase(xForwardedFor)) {
                return xForwardedFor;
            }
        }
        
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty() && !"unknown".equalsIgnoreCase(xRealIP)) {
            return xRealIP.trim();
        }
        
        return request.getRemoteAddr();
    }
}
