package com.sleepstory.backend.api.controller;

import com.sleepstory.backend.api.dto.*;
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

    /**
     * 用户注册
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
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public Result<AuthResponse.UserInfo> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.unauthorized("未提供有效的认证信息");
            }

            String token = authHeader.substring(7);
            // TODO: 从token中解析userId
            // 这里简化处理，实际应该使用JWT解析
            String userId = extractUserIdFromToken(token);

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
     */
    @PostMapping("/refresh")
    public Result<AuthResponse> refreshToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.unauthorized("未提供有效的认证信息");
            }

            String token = authHeader.substring(7);
            String userId = extractUserIdFromToken(token);

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
     */
    @GetMapping("/check-phone")
    public Result<Boolean> checkPhone(@RequestParam String phone) {
        try {
            // TODO: 实现检查逻辑
            return Result.success(false);
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
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * 从Token中提取用户ID
     * TODO: 实际应该使用JWT解析
     */
    private String extractUserIdFromToken(String token) {
        // 临时实现，实际应该解析JWT
        return token;
    }
}
