package service

import (
	"context"
	"fmt"
	"time"

	"github.com/sleepstory/backend/internal/application/dto"
	"github.com/sleepstory/backend/internal/domain/entity"
	"github.com/sleepstory/backend/internal/domain/repository"
	"github.com/sleepstory/backend/internal/infrastructure/security"
)

// AuthService 认证服务
type AuthService struct {
	userRepo        repository.UserRepository
	profileRepo     repository.UserProfileRepository
	passwordEncoder *security.PasswordEncoder
	jwtProvider     *security.JWTTokenProvider
}

// NewAuthService 创建认证服务
func NewAuthService(
	userRepo repository.UserRepository,
	profileRepo repository.UserProfileRepository,
	passwordEncoder *security.PasswordEncoder,
	jwtProvider *security.JWTTokenProvider,
) *AuthService {
	return &AuthService{
		userRepo:        userRepo,
		profileRepo:     profileRepo,
		passwordEncoder: passwordEncoder,
		jwtProvider:     jwtProvider,
	}
}

// Register 用户注册
func (s *AuthService) Register(ctx context.Context, req dto.RegisterRequest) (*dto.AuthResponse, error) {
	// 检查手机号是否已注册
	exists, err := s.userRepo.ExistsByPhone(ctx, req.Phone)
	if err != nil {
		return nil, fmt.Errorf("检查手机号失败: %w", err)
	}
	if exists {
		return nil, fmt.Errorf("手机号已注册")
	}

	// 加密密码
	passwordHash, err := s.passwordEncoder.Encode(req.Password)
	if err != nil {
		return nil, fmt.Errorf("密码加密失败: %w", err)
	}

	// 生成默认昵称
	nickname := req.Nickname
	if nickname == "" {
		nickname = fmt.Sprintf("晚安旅人%d", time.Now().Unix()%10000)
	}

	// 创建用户
	user := entity.NewUser(req.Phone, passwordHash, nickname)
	if err := s.userRepo.Create(ctx, user); err != nil {
		return nil, fmt.Errorf("创建用户失败: %w", err)
	}

	// 创建用户资料
	profile := entity.NewUserProfile(user.ID)
	if err := s.profileRepo.Create(ctx, profile); err != nil {
		return nil, fmt.Errorf("创建用户资料失败: %w", err)
	}

	// 生成JWT
	token, err := s.jwtProvider.GenerateToken(user.ID, user.Phone)
	if err != nil {
		return nil, fmt.Errorf("生成令牌失败: %w", err)
	}

	return &dto.AuthResponse{
		Token:     token,
		TokenType: "Bearer",
		ExpiresIn: 86400,
		User: dto.UserInfo{
			ID:        user.ID,
			Phone:     user.MaskPhone(),
			Nickname:  user.Nickname,
			AvatarURL: user.AvatarURL,
			CreatedAt: user.CreatedAt,
		},
	}, nil
}

// Login 用户登录
func (s *AuthService) Login(ctx context.Context, req dto.LoginRequest, clientIP string) (*dto.AuthResponse, error) {
	// 查找用户
	user, err := s.userRepo.FindByPhone(ctx, req.Phone)
	if err != nil {
		return nil, fmt.Errorf("用户不存在")
	}

	// 检查用户状态
	if !user.IsActive() {
		return nil, fmt.Errorf("用户已被锁定或禁用")
	}

	// 验证密码
	if !s.passwordEncoder.Matches(req.Password, user.PasswordHash) {
		return nil, fmt.Errorf("密码错误")
	}

	// 更新登录信息
	user.RecordLogin(clientIP)
	if err := s.userRepo.Update(ctx, user); err != nil {
		return nil, fmt.Errorf("更新登录信息失败: %w", err)
	}

	// 生成JWT
	token, err := s.jwtProvider.GenerateToken(user.ID, user.Phone)
	if err != nil {
		return nil, fmt.Errorf("生成令牌失败: %w", err)
	}

	return &dto.AuthResponse{
		Token:     token,
		TokenType: "Bearer",
		ExpiresIn: 86400,
		User: dto.UserInfo{
			ID:        user.ID,
			Phone:     user.MaskPhone(),
			Nickname:  user.Nickname,
			AvatarURL: user.AvatarURL,
			CreatedAt: user.CreatedAt,
		},
	}, nil
}

// GetUserInfo 获取用户信息
func (s *AuthService) GetUserInfo(ctx context.Context, userID string) (*dto.UserInfo, error) {
	user, err := s.userRepo.FindByID(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("用户不存在")
	}

	return &dto.UserInfo{
		ID:        user.ID,
		Phone:     user.MaskPhone(),
		Nickname:  user.Nickname,
		AvatarURL: user.AvatarURL,
		CreatedAt: user.CreatedAt,
	}, nil
}

// RefreshToken 刷新令牌
func (s *AuthService) RefreshToken(ctx context.Context, userID string) (*dto.AuthResponse, error) {
	user, err := s.userRepo.FindByID(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("用户不存在")
	}

	token, err := s.jwtProvider.GenerateToken(user.ID, user.Phone)
	if err != nil {
		return nil, fmt.Errorf("生成令牌失败: %w", err)
	}

	return &dto.AuthResponse{
		Token:     token,
		TokenType: "Bearer",
		ExpiresIn: 86400,
		User: dto.UserInfo{
			ID:        user.ID,
			Phone:     user.MaskPhone(),
			Nickname:  user.Nickname,
			AvatarURL: user.AvatarURL,
			CreatedAt: user.CreatedAt,
		},
	}, nil
}
