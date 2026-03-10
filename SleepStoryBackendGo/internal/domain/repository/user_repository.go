package repository

import (
	"context"

	"github.com/sleepstory/backend/internal/domain/entity"
)

// UserRepository 用户仓储接口
type UserRepository interface {
	Create(ctx context.Context, user *entity.User) error
	Update(ctx context.Context, user *entity.User) error
	Delete(ctx context.Context, id string) error
	FindByID(ctx context.Context, id string) (*entity.User, error)
	FindByPhone(ctx context.Context, phone string) (*entity.User, error)
	FindByEmail(ctx context.Context, email string) (*entity.User, error)
	ExistsByPhone(ctx context.Context, phone string) (bool, error)
	ExistsByEmail(ctx context.Context, email string) (bool, error)
}

// UserProfileRepository 用户资料仓储接口
type UserProfileRepository interface {
	Create(ctx context.Context, profile *entity.UserProfile) error
	Update(ctx context.Context, profile *entity.UserProfile) error
	Delete(ctx context.Context, userID string) error
	FindByUserID(ctx context.Context, userID string) (*entity.UserProfile, error)
}
