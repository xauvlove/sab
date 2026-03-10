package persistence

import (
	"fmt"
	"log"

	"github.com/sleepstory/backend/internal/domain/entity"
	"gorm.io/driver/mysql"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

// DB 数据库连接
type DB struct {
	*gorm.DB
}

// NewDatabase 创建数据库连接
func NewDatabase(dsn string, isMySQL bool) (*DB, error) {
	var dialector gorm.Dialector

	if isMySQL {
		dialector = mysql.Open(dsn)
	} else {
		dialector = sqlite.Open(dsn)
	}

	db, err := gorm.Open(dialector, &gorm.Config{
		Logger: logger.Default.LogMode(logger.Info),
	})
	if err != nil {
		return nil, fmt.Errorf("failed to connect database: %w", err)
	}

	return &DB{db}, nil
}

// AutoMigrate 自动迁移数据库表
func (db *DB) AutoMigrate() error {
	return db.DB.AutoMigrate(
		&entity.User{},
		&entity.UserProfile{},
		&entity.Story{},
		&entity.StoryGenerationTask{},
	)
}

// SeedData 初始化示例数据
func (db *DB) SeedData() error {
	var count int64
	db.Model(&entity.Story{}).Count(&count)
	if count > 0 {
		return nil // 已有数据，跳过
	}

	stories := []*entity.Story{
		{
			ID:          "story-001",
			Title:       "月光森林",
			Description: "在宁静的月光下，漫步在神秘的森林中，聆听大自然的声音",
			Category:    "NATURE",
			Duration:    "MEDIUM",
			Icon:        strPtr("🌲"),
			GradientColors: strPtr("#1a237e,#4a148c"),
			AudioURL:    strPtr("https://example.com/audio/forest.mp3"),
			IsGenerated: false,
			IsFavorite:  false,
			Rating:      float32Ptr(4.8),
			PlayCount:   1250,
			Content:     strPtr("夜幕降临，月光如水般洒在森林中..."),
		},
		{
			ID:          "story-002",
			Title:       "星空下的海滩",
			Description: "躺在柔软的沙滩上，仰望满天繁星，感受海浪的轻抚",
			Category:    "NATURE",
			Duration:    "SHORT",
			Icon:        strPtr("🏖️"),
			GradientColors: strPtr("#0d47a1,#1565c0"),
			AudioURL:    strPtr("https://example.com/audio/beach.mp3"),
			IsGenerated: false,
			IsFavorite:  false,
			Rating:      float32Ptr(4.9),
			PlayCount:   980,
			Content:     strPtr("海浪轻轻拍打着沙滩，发出有节奏的声响..."),
		},
		{
			ID:          "story-003",
			Title:       "云端城堡",
			Description: "漂浮在云端之上的神秘城堡，带你进入一个奇幻的梦境世界",
			Category:    "FANTASY",
			Duration:    "LONG",
			Icon:        strPtr("🏰"),
			GradientColors: strPtr("#7c4dff,#b388ff"),
			AudioURL:    strPtr("https://example.com/audio/castle.mp3"),
			IsGenerated: false,
			IsFavorite:  false,
			Rating:      float32Ptr(4.7),
			PlayCount:   756,
			Content:     strPtr("在云层之上，有一座晶莹剔透的城堡..."),
		},
		{
			ID:          "story-004",
			Title:       "正念呼吸",
			Description: "跟随引导进行深呼吸练习，放松身心，进入深度睡眠",
			Category:    "MEDITATION",
			Duration:    "SHORT",
			Icon:        strPtr("🧘"),
			GradientColors: strPtr("#00695c,#00897b"),
			AudioURL:    strPtr("https://example.com/audio/breathing.mp3"),
			IsGenerated: false,
			IsFavorite:  false,
			Rating:      float32Ptr(4.6),
			PlayCount:   543,
			Content:     strPtr("请找一个舒适的姿势躺下..."),
		},
		{
			ID:          "story-005",
			Title:       "雨夜小屋",
			Description: "窗外下着细雨，你在温暖的小屋里，听着雨声安然入睡",
			Category:    "NATURE",
			Duration:    "MEDIUM",
			Icon:        strPtr("🏠"),
			GradientColors: strPtr("#263238,#37474f"),
			AudioURL:    strPtr("https://example.com/audio/rain.mp3"),
			IsGenerated: false,
			IsFavorite:  true,
			Rating:      float32Ptr(4.9),
			PlayCount:   2100,
			Content:     strPtr("雨滴敲打着窗户，发出轻柔的声响..."),
		},
	}

	for _, story := range stories {
		if err := db.Create(story).Error; err != nil {
			log.Printf("Failed to seed story %s: %v", story.ID, err)
		}
	}

	return nil
}

func strPtr(s string) *string {
	return &s
}

func float32Ptr(f float32) *float32 {
	return &f
}
