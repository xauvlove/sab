package entity

import (
	"time"

	"github.com/google/uuid"
	"github.com/sleepstory/backend/internal/domain/valueobject"
)

// User 用户实体
type User struct {
	ID           string                `json:"id" gorm:"primaryKey;size:36"`
	Phone        string                `json:"phone" gorm:"uniqueIndex;size:20;not null"`
	Email        *string               `json:"email,omitempty" gorm:"uniqueIndex;size:100"`
	PasswordHash string                `json:"-" gorm:"column:password_hash;size:255;not null"`
	Nickname     string                `json:"nickname" gorm:"size:50"`
	AvatarURL    *string               `json:"avatar_url,omitempty" gorm:"column:avatar_url;size:500"`
	Status       valueobject.UserStatus `json:"status" gorm:"size:20;default:'ACTIVE'"`
	LastLoginAt  *time.Time            `json:"last_login_at,omitempty" gorm:"column:last_login_at"`
	LastLoginIP  *string               `json:"last_login_ip,omitempty" gorm:"column:last_login_ip;size:50"`
	CreatedAt    time.Time             `json:"created_at" gorm:"autoCreateTime"`
	UpdatedAt    time.Time             `json:"updated_at" gorm:"autoUpdateTime"`

	Profile *UserProfile `json:"profile,omitempty" gorm:"foreignKey:UserID"`
}

// TableName 指定表名
func (User) TableName() string {
	return "users"
}

// NewUser 创建新用户
func NewUser(phone, passwordHash, nickname string) *User {
	now := time.Now()
	return &User{
		ID:           uuid.New().String(),
		Phone:        phone,
		PasswordHash: passwordHash,
		Nickname:     nickname,
		Status:       valueobject.UserStatusActive,
		CreatedAt:    now,
		UpdatedAt:    now,
	}
}

// IsActive 检查用户是否活跃
func (u *User) IsActive() bool {
	return u.Status == valueobject.UserStatusActive
}

// IsLocked 检查用户是否被锁定
func (u *User) IsLocked() bool {
	return u.Status == valueobject.UserStatusLocked
}

// RecordLogin 记录登录信息
func (u *User) RecordLogin(ip string) {
	now := time.Now()
	u.LastLoginAt = &now
	u.LastLoginIP = &ip
	u.UpdatedAt = now
}

// Activate 激活用户
func (u *User) Activate() {
	u.Status = valueobject.UserStatusActive
	u.UpdatedAt = time.Now()
}

// Lock 锁定用户
func (u *User) Lock() {
	u.Status = valueobject.UserStatusLocked
	u.UpdatedAt = time.Now()
}

// MaskPhone 手机号脱敏
func (u *User) MaskPhone() string {
	if len(u.Phone) != 11 {
		return u.Phone
	}
	return u.Phone[:3] + "****" + u.Phone[7:]
}
