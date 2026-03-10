package main

import (
	"log"
	"os"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/sleepstory/backend/internal/application/service"
	"github.com/sleepstory/backend/internal/infrastructure/persistence"
	"github.com/sleepstory/backend/internal/infrastructure/security"
	httpHandler "github.com/sleepstory/backend/internal/interfaces/http"
	"github.com/sleepstory/backend/internal/interfaces/middleware"
)

func main() {
	// 设置时区
	os.Setenv("TZ", "Asia/Shanghai")

	// 数据库配置
	dsn := getEnv("DB_DSN", "file::memory:?cache=shared")
	isMySQL := getEnv("DB_TYPE", "sqlite") == "mysql"

	// 连接数据库
	db, err := persistence.NewDatabase(dsn, isMySQL)
	if err != nil {
		log.Fatalf("Failed to connect database: %v", err)
	}

	// 自动迁移
	if err := db.AutoMigrate(); err != nil {
		log.Fatalf("Failed to migrate database: %v", err)
	}

	// 初始化示例数据
	if err := db.SeedData(); err != nil {
		log.Printf("Failed to seed data: %v", err)
	}

	// 初始化依赖
	userRepo := persistence.NewUserRepository(db.DB)
	profileRepo := persistence.NewUserProfileRepository(db.DB)
	storyRepo := persistence.NewStoryRepository(db.DB)
	taskRepo := persistence.NewStoryGenerationTaskRepository(db.DB)

	passwordEncoder := security.NewPasswordEncoder()
	jwtConfig := security.JWTConfig{
		Secret:     getEnv("JWT_SECRET", "SleepStorySecretKeyForJwtTokenGeneration2024"),
		Expiration: 24 * time.Hour,
	}
	jwtProvider := security.NewJWTTokenProvider(jwtConfig)

	// 初始化服务
	authService := service.NewAuthService(userRepo, profileRepo, passwordEncoder, jwtProvider)
	storyService := service.NewStoryService(storyRepo, taskRepo)

	// 初始化处理器
	authHandler := httpHandler.NewAuthHandler(authService)
	storyHandler := httpHandler.NewStoryHandler(storyService)
	authMiddleware := middleware.NewAuthMiddleware(jwtProvider)

	// 设置Gin
	if os.Getenv("GIN_MODE") == "release" {
		gin.SetMode(gin.ReleaseMode)
	}
	r := gin.Default()

	// 健康检查
	r.GET("/health", func(c *gin.Context) {
		c.JSON(200, gin.H{"status": "ok"})
	})

	// API路由组
	api := r.Group("/api")
	{
		// 认证路由
		auth := api.Group("/auth")
		{
			auth.POST("/register", authHandler.Register)
			auth.POST("/login", authHandler.Login)
			auth.GET("/me", authMiddleware.RequireAuth(), authHandler.GetMe)
			auth.POST("/refresh", authMiddleware.RequireAuth(), authHandler.RefreshToken)
		}

		// 故事路由
		stories := api.Group("/stories")
		{
			stories.GET("", storyHandler.GetRecommendedStories)
			stories.GET("/top", storyHandler.GetTopStories)
			stories.GET("/search", storyHandler.SearchStories)
			stories.GET("/favorites", storyHandler.GetFavorites)
			stories.GET("/category/:category", storyHandler.GetStoriesByCategory)
			stories.GET("/:id", storyHandler.GetStory)
			stories.POST("/:id/play", storyHandler.IncrementPlayCount)
			stories.POST("/:id/favorite", storyHandler.ToggleFavorite)
		}

		// 生成任务路由
		tasks := api.Group("/generation-tasks")
		{
			tasks.POST("", authMiddleware.OptionalAuth(), storyHandler.CreateGenerationTask)
			tasks.GET("/:id", storyHandler.GetTaskProgress)
		}
	}

	// 启动服务器
	port := getEnv("PORT", "8080")
	log.Printf("Server starting on port %s", port)
	if err := r.Run(":" + port); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
