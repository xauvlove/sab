package valueobject

// UserStatus 用户状态
type UserStatus string

const (
	UserStatusPending  UserStatus = "PENDING"
	UserStatusActive   UserStatus = "ACTIVE"
	UserStatusLocked   UserStatus = "LOCKED"
	UserStatusDisabled UserStatus = "DISABLED"
)

// StoryCategory 故事分类
type StoryCategory string

const (
	StoryCategoryNature     StoryCategory = "NATURE"
	StoryCategoryFantasy    StoryCategory = "FANTASY"
	StoryCategoryMeditation StoryCategory = "MEDITATION"
	StoryCategoryAdventure  StoryCategory = "ADVENTURE"
)

// StoryDuration 故事时长
type StoryDuration string

const (
	StoryDurationShort  StoryDuration = "SHORT"
	StoryDurationMedium StoryDuration = "MEDIUM"
	StoryDurationLong   StoryDuration = "LONG"
)

// StoryScene 故事场景
type StoryScene string

const (
	StorySceneMoonlight StoryScene = "MOONLIGHT"
	StorySceneMountain  StoryScene = "MOUNTAIN"
	StorySceneBeach     StoryScene = "BEACH"
	StorySceneForest    StoryScene = "FOREST"
	StorySceneRain      StoryScene = "RAIN"
	StorySceneSpace     StoryScene = "SPACE"
)

// StoryMood 故事基调
type StoryMood string

const (
	StoryMoodCalm   StoryMood = "CALM"
	StoryMoodWarm   StoryMood = "WARM"
	StoryMoodFantasy StoryMood = "FANTASY"
	StoryMoodPeaceful StoryMood = "PEACEFUL"
)

// GenerationStatus 生成状态
type GenerationStatus string

const (
	GenerationStatusPending    GenerationStatus = "PENDING"
	GenerationStatusProcessing GenerationStatus = "PROCESSING"
	GenerationStatusCompleted  GenerationStatus = "COMPLETED"
	GenerationStatusFailed     GenerationStatus = "FAILED"
)

// Gender 性别
type Gender string

const (
	GenderUnknown Gender = "UNKNOWN"
	GenderMale    Gender = "MALE"
	GenderFemale  Gender = "FEMALE"
	GenderOther   Gender = "OTHER"
)

// DurationSeconds 获取时长的秒数
func (d StoryDuration) DurationSeconds() int {
	switch d {
	case StoryDurationShort:
		return 300 // 5分钟
	case StoryDurationMedium:
		return 600 // 10分钟
	case StoryDurationLong:
		return 900 // 15分钟
	default:
		return 600
	}
}

// DisplayName 获取显示名称
func (c StoryCategory) DisplayName() string {
	switch c {
	case StoryCategoryNature:
		return "自然"
	case StoryCategoryFantasy:
		return "奇幻"
	case StoryCategoryMeditation:
		return "冥想"
	case StoryCategoryAdventure:
		return "冒险"
	default:
		return string(c)
	}
}

// DisplayName 获取显示名称
func (d StoryDuration) DisplayName() string {
	switch d {
	case StoryDurationShort:
		return "短篇"
	case StoryDurationMedium:
		return "中篇"
	case StoryDurationLong:
		return "长篇"
	default:
		return string(d)
	}
}
