package repository

import (
	"context"

	"github.com/sleepstory/backend/internal/domain/entity"
	"github.com/sleepstory/backend/internal/domain/valueobject"
)

// StoryRepository 故事仓储接口
type StoryRepository interface {
	Create(ctx context.Context, story *entity.Story) error
	Update(ctx context.Context, story *entity.Story) error
	Delete(ctx context.Context, id string) error
	FindByID(ctx context.Context, id string) (*entity.Story, error)
	FindByCategory(ctx context.Context, category valueobject.StoryCategory) ([]*entity.Story, error)
	FindRecommended(ctx context.Context, limit int) ([]*entity.Story, error)
	FindTopStories(ctx context.Context, limit int) ([]*entity.Story, error)
	SearchByKeyword(ctx context.Context, keyword string) ([]*entity.Story, error)
	FindFavorites(ctx context.Context) ([]*entity.Story, error)
	IncrementPlayCount(ctx context.Context, id string) error
	UpdateFavoriteStatus(ctx context.Context, id string, isFavorite bool) error
}

// StoryGenerationTaskRepository 任务仓储接口
type StoryGenerationTaskRepository interface {
	Create(ctx context.Context, task *entity.StoryGenerationTask) error
	Update(ctx context.Context, task *entity.StoryGenerationTask) error
	Delete(ctx context.Context, id string) error
	FindByID(ctx context.Context, id string) (*entity.StoryGenerationTask, error)
	FindByUserID(ctx context.Context, userID string) ([]*entity.StoryGenerationTask, error)
	FindByStatus(ctx context.Context, status valueobject.GenerationStatus) ([]*entity.StoryGenerationTask, error)
	FindLatestByUserID(ctx context.Context, userID string) (*entity.StoryGenerationTask, error)
	CountByStatus(ctx context.Context, status valueobject.GenerationStatus) (int64, error)
}
