package entity

import (
	"time"

	"github.com/google/uuid"
	"github.com/sleepstory/backend/internal/domain/valueobject"
)

// StoryGenerationTask 故事生成任务实体
type StoryGenerationTask struct {
	ID              string                         `json:"id" gorm:"primaryKey;size:36"`
	UserID          *string                        `json:"user_id,omitempty" gorm:"column:user_id;size:36;index"`
	Keywords        *string                        `json:"keywords,omitempty" gorm:"size:500"`
	Scene           *valueobject.StoryScene        `json:"scene,omitempty" gorm:"size:50"`
	Mood            *valueobject.StoryMood         `json:"mood,omitempty" gorm:"size:50"`
	Duration        valueobject.StoryDuration      `json:"duration" gorm:"size:20;not null"`
	Status          valueobject.GenerationStatus   `json:"status" gorm:"size:50;not null"`
	StoryID         *string                        `json:"story_id,omitempty" gorm:"column:story_id;size:36;index"`
	ErrorMessage    *string                        `json:"error_message,omitempty" gorm:"column:error_message;size:1000"`
	ProgressPercent int                            `json:"progress_percent" gorm:"column:progress_percent;default:0"`
	StatusMessage   *string                        `json:"status_message,omitempty" gorm:"column:status_message;size:200"`
	CreatedAt       time.Time                      `json:"created_at" gorm:"autoCreateTime"`
	UpdatedAt       time.Time                      `json:"updated_at" gorm:"autoUpdateTime"`
	CompletedAt     *time.Time                     `json:"completed_at,omitempty" gorm:"column:completed_at"`

	Story *Story `json:"story,omitempty" gorm:"foreignKey:StoryID"`
}

// TableName 指定表名
func (StoryGenerationTask) TableName() string {
	return "story_generation_tasks"
}

// NewStoryGenerationTask 创建新任务
func NewStoryGenerationTask(userID *string, keywords *string, scene *valueobject.StoryScene, mood *valueobject.StoryMood, duration valueobject.StoryDuration) *StoryGenerationTask {
	now := time.Now()
	statusMessage := "等待处理..."
	return &StoryGenerationTask{
		ID:              uuid.New().String(),
		UserID:          userID,
		Keywords:        keywords,
		Scene:           scene,
		Mood:            mood,
		Duration:        duration,
		Status:          valueobject.GenerationStatusPending,
		ProgressPercent: 0,
		StatusMessage:   &statusMessage,
		CreatedAt:       now,
		UpdatedAt:       now,
	}
}

// StartProcessing 开始处理
func (t *StoryGenerationTask) StartProcessing() {
	status := "开始生成故事..."
	t.Status = valueobject.GenerationStatusProcessing
	t.ProgressPercent = 0
	t.StatusMessage = &status
	t.UpdatedAt = time.Now()
}

// UpdateProgress 更新进度
func (t *StoryGenerationTask) UpdateProgress(percent int, message string) {
	t.ProgressPercent = percent
	t.StatusMessage = &message
	t.UpdatedAt = time.Now()
}

// MarkCompleted 标记完成
func (t *StoryGenerationTask) MarkCompleted(storyID string) {
	status := "故事生成完成"
	now := time.Now()
	t.Status = valueobject.GenerationStatusCompleted
	t.StoryID = &storyID
	t.ProgressPercent = 100
	t.StatusMessage = &status
	t.CompletedAt = &now
	t.UpdatedAt = now
}

// MarkFailed 标记失败
func (t *StoryGenerationTask) MarkFailed(errorMessage string) {
	status := "生成失败: " + errorMessage
	t.Status = valueobject.GenerationStatusFailed
	t.ErrorMessage = &errorMessage
	t.StatusMessage = &status
	t.UpdatedAt = time.Now()
}
