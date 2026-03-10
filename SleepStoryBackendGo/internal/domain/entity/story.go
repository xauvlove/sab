package entity

import (
	"time"

	"github.com/google/uuid"
	"github.com/sleepstory/backend/internal/domain/valueobject"
)

// Story 故事实体
type Story struct {
	ID             string                   `json:"id" gorm:"primaryKey;size:36"`
	Title          string                   `json:"title" gorm:"size:200;not null"`
	Description    string                   `json:"description" gorm:"size:500;not null"`
	Category       valueobject.StoryCategory `json:"category" gorm:"size:50;not null"`
	Duration       valueobject.StoryDuration `json:"duration" gorm:"size:20;not null"`
	DurationSeconds *int                    `json:"duration_seconds,omitempty" gorm:"column:duration_seconds"`
	Icon           *string                  `json:"icon,omitempty" gorm:"size:10"`
	GradientColors *string                  `json:"gradient_colors,omitempty" gorm:"column:gradient_colors;size:200"`
	AudioURL       *string                  `json:"audio_url,omitempty" gorm:"column:audio_url;size:500"`
	IsGenerated    bool                     `json:"is_generated" gorm:"column:is_generated;default:false"`
	IsFavorite     bool                     `json:"is_favorite" gorm:"column:is_favorite;default:false"`
	Rating         *float32                 `json:"rating,omitempty"`
	PlayCount      int                      `json:"play_count" gorm:"column:play_count;default:0"`
	Content        *string                  `json:"content,omitempty" gorm:"type:text"`
	CreatedAt      time.Time                `json:"created_at" gorm:"autoCreateTime"`
	UpdatedAt      time.Time                `json:"updated_at" gorm:"autoUpdateTime"`
}

// TableName 指定表名
func (Story) TableName() string {
	return "stories"
}

// NewStory 创建新故事
func NewStory(title, description string, category valueobject.StoryCategory, duration valueobject.StoryDuration) *Story {
	return &Story{
		ID:          uuid.New().String(),
		Title:       title,
		Description: description,
		Category:    category,
		Duration:    duration,
		IsGenerated: false,
		IsFavorite:  false,
		PlayCount:   0,
	}
}

// NewGeneratedStory 创建AI生成的故事
func NewGeneratedStory(title, description, content string, duration valueobject.StoryDuration, audioURL string) *Story {
	return &Story{
		ID:          uuid.New().String(),
		Title:       title,
		Description: description,
		Category:    valueobject.StoryCategoryFantasy,
		Duration:    duration,
		Content:     &content,
		AudioURL:    &audioURL,
		IsGenerated: true,
		IsFavorite:  false,
		PlayCount:   0,
	}
}

// IncrementPlayCount 增加播放次数
func (s *Story) IncrementPlayCount() {
	s.PlayCount++
}

// ToggleFavorite 切换收藏状态
func (s *Story) ToggleFavorite() {
	s.IsFavorite = !s.IsFavorite
}
