package entity

import (
	"time"

	"github.com/sleepstory/backend/internal/domain/valueobject"
)

// UserProfile 用户资料实体
type UserProfile struct {
	UserID                string                   `json:"user_id" gorm:"primaryKey;size:36"`
	SleepTime             string                   `json:"sleep_time" gorm:"column:sleep_time;size:10;default:'22:30'"`
	PreferredDuration     *valueobject.StoryDuration `json:"preferred_duration,omitempty" gorm:"column:preferred_duration;size:20"`
	PreferredCategories   string                   `json:"preferred_categories,omitempty" gorm:"column:preferred_categories;size:200"`
	StreakDays            int                      `json:"streak_days" gorm:"column:streak_days;default:0"`
	TotalStoriesListened  int                      `json:"total_stories_listened" gorm:"column:total_stories_listened;default:0"`
	TotalListeningMinutes int                      `json:"total_listening_minutes" gorm:"column:total_listening_minutes;default:0"`
	LastListenedAt        *time.Time               `json:"last_listened_at,omitempty" gorm:"column:last_listened_at"`
	CreatedAt             time.Time                `json:"created_at" gorm:"autoCreateTime"`
	UpdatedAt             time.Time                `json:"updated_at" gorm:"autoUpdateTime"`
}

// TableName 指定表名
func (UserProfile) TableName() string {
	return "user_profiles"
}

// NewUserProfile 创建新用户资料
func NewUserProfile(userID string) *UserProfile {
	medium := valueobject.StoryDurationMedium
	return &UserProfile{
		UserID:                userID,
		SleepTime:             "22:30",
		PreferredDuration:     &medium,
		StreakDays:            0,
		TotalStoriesListened:  0,
		TotalListeningMinutes: 0,
	}
}

// IncrementStreak 增加连续天数
func (p *UserProfile) IncrementStreak() {
	p.StreakDays++
	p.UpdatedAt = time.Now()
}

// ResetStreak 重置连续天数
func (p *UserProfile) ResetStreak() {
	p.StreakDays = 0
	p.UpdatedAt = time.Now()
}

// RecordListening 记录收听
func (p *UserProfile) RecordListening(minutes int) {
	p.TotalStoriesListened++
	p.TotalListeningMinutes += minutes
	now := time.Now()
	p.LastListenedAt = &now
	p.UpdatedAt = now
}
