package dto

import "time"

// StoryResponse 故事响应
type StoryResponse struct {
	ID             string    `json:"id"`
	Title          string    `json:"title"`
	Description    string    `json:"description"`
	Category       string    `json:"category"`
	Duration       string    `json:"duration"`
	DurationSeconds *int     `json:"duration_seconds,omitempty"`
	Icon           *string   `json:"icon,omitempty"`
	GradientColors *string   `json:"gradient_colors,omitempty"`
	AudioURL       *string   `json:"audio_url,omitempty"`
	IsGenerated    bool      `json:"is_generated"`
	IsFavorite     bool      `json:"is_favorite"`
	Rating         *float32  `json:"rating,omitempty"`
	PlayCount      int       `json:"play_count"`
	Content        *string   `json:"content,omitempty"`
	CreatedAt      time.Time `json:"created_at"`
}

// CreateStoryRequest 创建故事请求
type CreateStoryRequest struct {
	Title       string `json:"title" binding:"required"`
	Description string `json:"description" binding:"required"`
	Category    string `json:"category" binding:"required"`
	Duration    string `json:"duration" binding:"required"`
	Content     string `json:"content"`
}

// GenerateStoryRequest 生成故事请求
type GenerateStoryRequest struct {
	Keywords *string `json:"keywords,omitempty"`
	Scene    *string `json:"scene,omitempty"`
	Mood     *string `json:"mood,omitempty"`
	Duration string  `json:"duration" binding:"required"`
}

// StoryGenerationTaskResponse 任务响应
type StoryGenerationTaskResponse struct {
	ID              string     `json:"id"`
	Status          string     `json:"status"`
	ProgressPercent int        `json:"progress_percent"`
	StatusMessage   *string    `json:"status_message,omitempty"`
	StoryID         *string    `json:"story_id,omitempty"`
	ErrorMessage    *string    `json:"error_message,omitempty"`
	CreatedAt       time.Time  `json:"created_at"`
	CompletedAt     *time.Time `json:"completed_at,omitempty"`
}

// TaskProgressResponse 任务进度响应
type TaskProgressResponse struct {
	TaskID    string `json:"task_id"`
	Status    string `json:"status"`
	Progress  int    `json:"progress"`
	Message   string `json:"message"`
	StoryID   string `json:"story_id,omitempty"`
}
