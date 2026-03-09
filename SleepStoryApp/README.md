# 眠语 SleepStory - AI助眠小说App

一款专为失眠人群设计的Android应用，通过AI生成个性化的助眠故事，帮助用户放松身心、安然入睡。

## 功能特性

### 核心功能
- 🤖 **AI故事生成** - 根据用户偏好实时生成短篇、中篇、长篇小说
- 🎯 **个性化评估** - 通过问卷了解用户睡眠习惯和故事偏好
- 🎵 **智能配音** - 舒缓的AI语音配合白噪音背景
- ⏰ **睡眠模式** - 智能定时关闭，渐进式音量降低
- 📊 **睡眠追踪** - 记录使用数据，生成睡眠报告

### 故事类型
- 🌲 自然探索 - 森林、海洋、山川
- 🏰 奇幻冒险 - 魔法、传说、异世界
- 🧘 冥想疗愈 - 正念、放松、治愈
- 🚀 科幻未来 - 太空、科技、未来
- 📚 经典文学 - 名著、诗歌、散文
- 💕 温暖治愈 - 爱情、友情、日常

## 技术架构

### 技术栈
- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **本地存储**: Room + DataStore
- **网络**: Retrofit + OkHttp
- **音频**: ExoPlayer
- **异步**: Kotlin Coroutines + Flow

### 项目结构
```
app/src/main/java/com/sleepstory/app/
├── data/
│   ├── api/          # API接口
│   ├── model/        # 数据模型
│   ├── repository/   # 数据仓库
│   └── local/        # 本地数据库
├── ui/
│   ├── components/   # 通用组件
│   ├── navigation/   # 导航
│   ├── screens/      # 页面
│   │   ├── splash/       # 启动页
│   │   ├── welcome/      # 欢迎页
│   │   ├── assessment/   # 睡眠评估
│   │   ├── home/         # 首页
│   │   ├── generate/     # 故事生成
│   │   ├── generating/   # 生成中
│   │   ├── player/       # 播放页
│   │   ├── discover/     # 发现页
│   │   └── profile/      # 个人中心
│   └── theme/        # 主题
├── service/          # 后台服务
└── utils/            # 工具类
```

## 页面说明

### 1. 启动页 (SplashScreen)
- 品牌Logo展示
- 呼吸动画效果
- 自动跳转逻辑（新用户→欢迎页，老用户→首页）

### 2. 欢迎页 (WelcomeScreen)
- 应用介绍
- 功能亮点展示
- 开始体验按钮

### 3. 睡眠评估 (AssessmentScreen)
- 5步问卷评估
  - 入睡时间
  - 入睡时长
  - 故事类型偏好
  - 理想故事时长
  - 背景音乐偏好
- 进度指示器

### 4. 首页 (HomeScreen)
- 个性化问候
- 快速入睡模式
- 分类标签筛选
- 推荐故事列表

### 5. AI故事生成 (GenerateScreen)
- 关键词输入
- 场景选择（月夜、山间、海边等）
- 故事基调调节
- 故事长度选择

### 6. 生成中页面 (GeneratingScreen)
- 动画效果
- 进度显示
- 状态提示

### 7. 播放页 (PlayerScreen)
- 故事封面动画
- 播放控制（播放/暂停/上一首/下一首）
- 进度条
- 收藏/定时/分享/语速调节
- 睡眠模式开关

### 8. 发现页 (DiscoverScreen)
- 搜索功能
- 热门标签
- 分类浏览
- 排行榜

### 9. 个人中心 (ProfileScreen)
- 用户信息
- 睡眠统计
- 功能菜单（睡眠提醒、收藏、报告、设置）
- 关于应用

## 设计特点

### 视觉设计
- **配色方案**: 深色系配色，深蓝紫色渐变
  - 主背景: #0F0F23
  - 次背景: #1A1A3E
  - 主色: #8B5CF6 (紫色)
  - 强调色: #3B82F6 (蓝色)
- **动画效果**: 呼吸动画、波浪动画、星空闪烁
- **圆角设计**: 统一的圆角风格，柔和视觉

### 交互设计
- 底部导航栏，5个主要入口
- 卡片式布局，信息层次清晰
- 玻璃拟态效果，现代感强
- 流畅的页面过渡动画

## 数据模型

### Story（故事）
```kotlin
data class Story(
    val id: String,
    val title: String,
    val description: String,
    val category: StoryCategory,
    val duration: Int,
    val icon: String,
    val gradientColors: List<String>,
    val audioUrl: String?,
    val isGenerated: Boolean,
    val isFavorite: Boolean,
    val rating: Float,
    val playCount: Int
)
```

### UserProfile（用户档案）
```kotlin
data class UserProfile(
    val name: String,
    val sleepTime: String,
    val preferredDuration: StoryDuration,
    val preferredCategories: List<StoryCategory>,
    val onboardingCompleted: Boolean,
    val streakDays: Int,
    val totalStoriesListened: Int
)
```

## 后端集成建议

### AI故事生成
```kotlin
// 使用OpenAI API或类似服务
interface StoryApiService {
    @POST("api/stories/generate")
    suspend fun generateStory(
        @Body request: StoryGenerationRequest
    ): Response<StoryContent>
}
```

### TTS语音合成
- Azure Text-to-Speech
- Google Cloud Text-to-Speech
- ElevenLabs

### 音频存储
- AWS S3 / 阿里云OSS
- CDN加速分发

## 本地开发

### 环境要求
- Android Studio Hedgehog (2023.1.1) 或更高
- JDK 17
- Android SDK 34
- Kotlin 1.9.20

### 构建步骤
1. 克隆项目
```bash
git clone <repository-url>
cd SleepStoryApp
```

2. 使用Android Studio打开项目

3. 同步Gradle依赖
```bash
./gradlew sync
```

4. 运行应用
```bash
./gradlew installDebug
```

## 后续优化方向

### 功能增强
- [ ] 离线下载功能
- [ ] 多语言支持
- [ ] 社交分享功能
- [ ] 智能推荐算法优化
- [ ] 睡眠质量监测（结合可穿戴设备）

### 技术优化
- [ ] 音频预加载
- [ ] 图片懒加载
- [ ] 数据库索引优化
- [ ] 内存泄漏检测
- [ ] 单元测试覆盖

## 开源协议

MIT License

## 联系方式

如有问题或建议，欢迎通过以下方式联系：
- 邮箱: support@sleepstory.app
- 微信: SleepStoryApp

---

**眠语** - 用故事编织梦境，让心灵归于宁静 🌙