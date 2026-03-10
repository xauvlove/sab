package http

import (
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"github.com/sleepstory/backend/internal/application/dto"
	"github.com/sleepstory/backend/internal/application/service"
)

// StoryHandler 故事处理器
type StoryHandler struct {
	storyService *service.StoryService
}

// NewStoryHandler 创建故事处理器
func NewStoryHandler(storyService *service.StoryService) *StoryHandler {
	return &StoryHandler{storyService: storyService}
}

// GetStory 获取故事详情
func (h *StoryHandler) GetStory(c *gin.Context) {
	id := c.Param("id")
	story, err := h.storyService.GetStoryByID(c.Request.Context(), id)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"code": 404, "message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success", "data": story})
}

// GetStoriesByCategory 根据分类获取故事
func (h *StoryHandler) GetStoriesByCategory(c *gin.Context) {
	category := c.Param("category")
	stories, err := h.storyService.GetStoriesByCategory(c.Request.Context(), category)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success", "data": stories})
}

// GetRecommendedStories 获取推荐故事
func (h *StoryHandler) GetRecommendedStories(c *gin.Context) {
	limitStr := c.DefaultQuery("limit", "10")
	limit, _ := strconv.Atoi(limitStr)
	if limit <= 0 || limit > 50 {
		limit = 10
	}

	stories, err := h.storyService.GetRecommendedStories(c.Request.Context(), limit)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success", "data": stories})
}

// GetTopStories 获取热门故事
func (h *StoryHandler) GetTopStories(c *gin.Context) {
	limitStr := c.DefaultQuery("limit", "10")
	limit, _ := strconv.Atoi(limitStr)
	if limit <= 0 || limit > 50 {
		limit = 10
	}

	stories, err := h.storyService.GetTopStories(c.Request.Context(), limit)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success", "data": stories})
}

// SearchStories 搜索故事
func (h *StoryHandler) SearchStories(c *gin.Context) {
	keyword := c.Query("keyword")
	if keyword == "" {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": "关键词不能为空"})
		return
	}

	stories, err := h.storyService.SearchStories(c.Request.Context(), keyword)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success", "data": stories})
}

// GetFavorites 获取收藏故事
func (h *StoryHandler) GetFavorites(c *gin.Context) {
	stories, err := h.storyService.GetFavorites(c.Request.Context())
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success", "data": stories})
}

// ToggleFavorite 切换收藏状态
func (h *StoryHandler) ToggleFavorite(c *gin.Context) {
	id := c.Param("id")
	if err := h.storyService.ToggleFavorite(c.Request.Context(), id); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success"})
}

// IncrementPlayCount 增加播放次数
func (h *StoryHandler) IncrementPlayCount(c *gin.Context) {
	id := c.Param("id")
	if err := h.storyService.IncrementPlayCount(c.Request.Context(), id); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success"})
}

// CreateGenerationTask 创建故事生成任务
func (h *StoryHandler) CreateGenerationTask(c *gin.Context) {
	var req dto.GenerateStoryRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}

	var userID *string
	if uid, exists := c.Get("userID"); exists {
		id := uid.(string)
		userID = &id
	}

	task, err := h.storyService.CreateGenerationTask(c.Request.Context(), userID, req)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"code": 400, "message": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success", "data": task})
}

// GetTaskProgress 获取任务进度
func (h *StoryHandler) GetTaskProgress(c *gin.Context) {
	id := c.Param("id")
	task, err := h.storyService.GetTaskByID(c.Request.Context(), id)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"code": 404, "message": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{"code": 200, "message": "success", "data": task})
}
