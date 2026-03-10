package dto

import "time"

// RegisterRequest 注册请求
type RegisterRequest struct {
	Phone    string `json:"phone" binding:"required,len=11"`
	Password string `json:"password" binding:"required,min=6,max=20"`
	Nickname string `json:"nickname"`
}

// LoginRequest 登录请求
type LoginRequest struct {
	Phone    string `json:"phone" binding:"required,len=11"`
	Password string `json:"password" binding:"required"`
}

// AuthResponse 认证响应
type AuthResponse struct {
	Token     string    `json:"token"`
	TokenType string    `json:"token_type"`
	ExpiresIn int64     `json:"expires_in"`
	User      UserInfo  `json:"user"`
}

// UserInfo 用户信息
type UserInfo struct {
	ID        string    `json:"id"`
	Phone     string    `json:"phone"`
	Nickname  string    `json:"nickname"`
	AvatarURL *string   `json:"avatar_url,omitempty"`
	CreatedAt time.Time `json:"created_at"`
}

// UserProfileResponse 用户资料响应
type UserProfileResponse struct {
	UserID                string   `json:"user_id"`
	SleepTime             string   `json:"sleep_time"`
	PreferredDuration     string   `json:"preferred_duration,omitempty"`
	PreferredCategories   []string `json:"preferred_categories,omitempty"`
	StreakDays            int      `json:"streak_days"`
	TotalStoriesListened  int      `json:"total_stories_listened"`
	TotalListeningMinutes int      `json:"total_listening_minutes"`
}
