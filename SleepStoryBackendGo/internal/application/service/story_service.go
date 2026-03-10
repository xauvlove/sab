package service

import (
	"context"
	"fmt"

	"github.com/sleepstory/backend/internal/application/dto"
	"github.com/sleepstory/backend/internal/domain/entity"
	"github.com/sleepstory/backend/internal/domain/repository"
	"github.com/sleepstory/backend/internal/domain/valueobject"
)

// StoryService 故事服务
type StoryService struct {
	storyRepo repository.StoryRepository
	taskRepo  repository.StoryGenerationTaskRepository
}

// NewStoryService 创建故事服务
func NewStoryService(
	storyRepo repository.StoryRepository,
	taskRepo repository.StoryGenerationTaskRepository,
) *StoryService {
	return &StoryService{
		storyRepo: storyRepo,
		taskRepo:  taskRepo,
	}
}

// GetStoryByID 根据ID获取故事
func (s *StoryService) GetStoryByID(ctx context.Context, id string) (*dto.StoryResponse, error) {
	story, err := s.storyRepo.FindByID(ctx, id)
	if err != nil {
		return nil, fmt.Errorf("故事不存在")
	}
	return toStoryResponse(story), nil
}

// GetStoriesByCategory 根据分类获取故事
func (s *StoryService) GetStoriesByCategory(ctx context.Context, category string) ([]*dto.StoryResponse, error) {
	stories, err := s.storyRepo.FindByCategory(ctx, valueobject.StoryCategory(category))
	if err != nil {
		return nil, fmt.Errorf("获取故事失败: %w", err)
	}
	return toStoryResponses(stories), nil
}

// GetRecommendedStories 获取推荐故事
func (s *StoryService) GetRecommendedStories(ctx context.Context, limit int) ([]*dto.StoryResponse, error) {
	stories, err := s.storyRepo.FindRecommended(ctx, limit)
	if err != nil {
		return nil, fmt.Errorf("获取推荐故事失败: %w", err)
	}
	return toStoryResponses(stories), nil
}

// GetTopStories 获取热门故事
func (s *StoryService) GetTopStories(ctx context.Context, limit int) ([]*dto.StoryResponse, error) {
	stories, err := s.storyRepo.FindTopStories(ctx, limit)
	if err != nil {
		return nil, fmt.Errorf("获取热门故事失败: %w", err)
	}
	return toStoryResponses(stories), nil
}

// SearchStories 搜索故事
func (s *StoryService) SearchStories(ctx context.Context, keyword string) ([]*dto.StoryResponse, error) {
	stories, err := s.storyRepo.SearchByKeyword(ctx, keyword)
	if err != nil {
		return nil, fmt.Errorf("搜索故事失败: %w", err)
	}
	return toStoryResponses(stories), nil
}

// GetFavorites 获取收藏故事
func (s *StoryService) GetFavorites(ctx context.Context) ([]*dto.StoryResponse, error) {
	stories, err := s.storyRepo.FindFavorites(ctx)
	if err != nil {
		return nil, fmt.Errorf("获取收藏故事失败: %w", err)
	}
	return toStoryResponses(stories), nil
}

// ToggleFavorite 切换收藏状态
func (s *StoryService) ToggleFavorite(ctx context.Context, id string) error {
	story, err := s.storyRepo.FindByID(ctx, id)
	if err != nil {
		return fmt.Errorf("故事不存在")
	}
	return s.storyRepo.UpdateFavoriteStatus(ctx, id, !story.IsFavorite)
}

// IncrementPlayCount 增加播放次数
func (s *StoryService) IncrementPlayCount(ctx context.Context, id string) error {
	return s.storyRepo.IncrementPlayCount(ctx, id)
}

// CreateGenerationTask 创建故事生成任务
func (s *StoryService) CreateGenerationTask(ctx context.Context, userID *string, req dto.GenerateStoryRequest) (*dto.StoryGenerationTaskResponse, error) {
	var scene *valueobject.StoryScene
	if req.Scene != nil {
		s := valueobject.StoryScene(*req.Scene)
		scene = &s
	}

	var mood *valueobject.StoryMood
	if req.Mood != nil {
		m := valueobject.StoryMood(*req.Mood)
		mood = &m
	}

	task := entity.NewStoryGenerationTask(
		userID,
		req.Keywords,
		scene,
		mood,
		valueobject.StoryDuration(req.Duration),
	)

	if err := s.taskRepo.Create(ctx, task); err != nil {
		return nil, fmt.Errorf("创建任务失败: %w", err)
	}

	return toTaskResponse(task), nil
}

// GetTaskByID 获取任务详情
func (s *StoryService) GetTaskByID(ctx context.Context, id string) (*dto.StoryGenerationTaskResponse, error) {
	task, err := s.taskRepo.FindByID(ctx, id)
	if err != nil {
		return nil, fmt.Errorf("任务不存在")
	}
	return toTaskResponse(task), nil
}

// Helper functions
func toStoryResponse(story *entity.Story) *dto.StoryResponse {
	return &dto.StoryResponse{
		ID:              story.ID,
		Title:           story.Title,
		Description:     story.Description,
		Category:        string(story.Category),
		Duration:        string(story.Duration),
		DurationSeconds: story.DurationSeconds,
		Icon:            story.Icon,
		GradientColors:  story.GradientColors,
		AudioURL:        story.AudioURL,
		IsGenerated:     story.IsGenerated,
		IsFavorite:      story.IsFavorite,
		Rating:          story.Rating,
		PlayCount:       story.PlayCount,
		Content:         story.Content,
		CreatedAt:       story.CreatedAt,
	}
}

func toStoryResponses(stories []*entity.Story) []*dto.StoryResponse {
	responses := make([]*dto.StoryResponse, len(stories))
	for i, story := range stories {
		responses[i] = toStoryResponse(story)
	}
	return responses
}

func toTaskResponse(task *entity.StoryGenerationTask) *dto.StoryGenerationTaskResponse {
	return &dto.StoryGenerationTaskResponse{
		ID:              task.ID,
		Status:          string(task.Status),
		ProgressPercent: task.ProgressPercent,
		StatusMessage:   task.StatusMessage,
		StoryID:         task.StoryID,
		ErrorMessage:    task.ErrorMessage,
		CreatedAt:       task.CreatedAt,
		CompletedAt:     task.CompletedAt,
	}
}
