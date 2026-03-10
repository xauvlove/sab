package persistence

import (
	"context"

	"github.com/sleepstory/backend/internal/domain/entity"
	"github.com/sleepstory/backend/internal/domain/repository"
	"github.com/sleepstory/backend/internal/domain/valueobject"
	"gorm.io/gorm"
)

// storyRepository 故事仓储实现
type storyRepository struct {
	db *gorm.DB
}

// NewStoryRepository 创建故事仓储
func NewStoryRepository(db *gorm.DB) repository.StoryRepository {
	return &storyRepository{db: db}
}

func (r *storyRepository) Create(ctx context.Context, story *entity.Story) error {
	return r.db.WithContext(ctx).Create(story).Error
}

func (r *storyRepository) Update(ctx context.Context, story *entity.Story) error {
	return r.db.WithContext(ctx).Save(story).Error
}

func (r *storyRepository) Delete(ctx context.Context, id string) error {
	return r.db.WithContext(ctx).Delete(&entity.Story{}, "id = ?", id).Error
}

func (r *storyRepository) FindByID(ctx context.Context, id string) (*entity.Story, error) {
	var story entity.Story
	err := r.db.WithContext(ctx).First(&story, "id = ?", id).Error
	if err != nil {
		return nil, err
	}
	return &story, nil
}

func (r *storyRepository) FindByCategory(ctx context.Context, category valueobject.StoryCategory) ([]*entity.Story, error) {
	var stories []*entity.Story
	err := r.db.WithContext(ctx).Where("category = ?", category).Order("created_at DESC").Find(&stories).Error
	return stories, err
}

func (r *storyRepository) FindRecommended(ctx context.Context, limit int) ([]*entity.Story, error) {
	var stories []*entity.Story
	err := r.db.WithContext(ctx).
		Where("is_generated = ?", false).
		Order("rating DESC, play_count DESC").
		Limit(limit).
		Find(&stories).Error
	return stories, err
}

func (r *storyRepository) FindTopStories(ctx context.Context, limit int) ([]*entity.Story, error) {
	var stories []*entity.Story
	err := r.db.WithContext(ctx).
		Order("play_count DESC").
		Limit(limit).
		Find(&stories).Error
	return stories, err
}

func (r *storyRepository) SearchByKeyword(ctx context.Context, keyword string) ([]*entity.Story, error) {
	var stories []*entity.Story
	pattern := "%" + keyword + "%"
	err := r.db.WithContext(ctx).
		Where("title LIKE ? OR description LIKE ?", pattern, pattern).
		Find(&stories).Error
	return stories, err
}

func (r *storyRepository) FindFavorites(ctx context.Context) ([]*entity.Story, error) {
	var stories []*entity.Story
	err := r.db.WithContext(ctx).
		Where("is_favorite = ?", true).
		Order("updated_at DESC").
		Find(&stories).Error
	return stories, err
}

func (r *storyRepository) IncrementPlayCount(ctx context.Context, id string) error {
	return r.db.WithContext(ctx).Model(&entity.Story{}).
		Where("id = ?", id).
		UpdateColumn("play_count", gorm.Expr("play_count + 1")).Error
}

func (r *storyRepository) UpdateFavoriteStatus(ctx context.Context, id string, isFavorite bool) error {
	return r.db.WithContext(ctx).Model(&entity.Story{}).
		Where("id = ?", id).
		Update("is_favorite", isFavorite).Error
}

// storyGenerationTaskRepository 任务仓储实现
type storyGenerationTaskRepository struct {
	db *gorm.DB
}

// NewStoryGenerationTaskRepository 创建任务仓储
func NewStoryGenerationTaskRepository(db *gorm.DB) repository.StoryGenerationTaskRepository {
	return &storyGenerationTaskRepository{db: db}
}

func (r *storyGenerationTaskRepository) Create(ctx context.Context, task *entity.StoryGenerationTask) error {
	return r.db.WithContext(ctx).Create(task).Error
}

func (r *storyGenerationTaskRepository) Update(ctx context.Context, task *entity.StoryGenerationTask) error {
	return r.db.WithContext(ctx).Save(task).Error
}

func (r *storyGenerationTaskRepository) Delete(ctx context.Context, id string) error {
	return r.db.WithContext(ctx).Delete(&entity.StoryGenerationTask{}, "id = ?", id).Error
}

func (r *storyGenerationTaskRepository) FindByID(ctx context.Context, id string) (*entity.StoryGenerationTask, error) {
	var task entity.StoryGenerationTask
	err := r.db.WithContext(ctx).Preload("Story").First(&task, "id = ?", id).Error
	if err != nil {
		return nil, err
	}
	return &task, nil
}

func (r *storyGenerationTaskRepository) FindByUserID(ctx context.Context, userID string) ([]*entity.StoryGenerationTask, error) {
	var tasks []*entity.StoryGenerationTask
	err := r.db.WithContext(ctx).
		Where("user_id = ?", userID).
		Order("created_at DESC").
		Find(&tasks).Error
	return tasks, err
}

func (r *storyGenerationTaskRepository) FindByStatus(ctx context.Context, status valueobject.GenerationStatus) ([]*entity.StoryGenerationTask, error) {
	var tasks []*entity.StoryGenerationTask
	err := r.db.WithContext(ctx).
		Where("status = ?", status).
		Order("created_at ASC").
		Find(&tasks).Error
	return tasks, err
}

func (r *storyGenerationTaskRepository) FindLatestByUserID(ctx context.Context, userID string) (*entity.StoryGenerationTask, error) {
	var task entity.StoryGenerationTask
	err := r.db.WithContext(ctx).
		Where("user_id = ?", userID).
		Order("created_at DESC").
		First(&task).Error
	if err != nil {
		return nil, err
	}
	return &task, nil
}

func (r *storyGenerationTaskRepository) CountByStatus(ctx context.Context, status valueobject.GenerationStatus) (int64, error) {
	var count int64
	err := r.db.WithContext(ctx).
		Model(&entity.StoryGenerationTask{}).
		Where("status = ?", status).
		Count(&count).Error
	return count, err
}
