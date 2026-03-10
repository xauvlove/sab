-- ============================================================
-- 睡眠故事APP数据库表结构
-- 适用于 MySQL 8.0+
-- ============================================================

-- -----------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(64) PRIMARY KEY COMMENT '用户ID',
    phone VARCHAR(20) UNIQUE NOT NULL COMMENT '手机号',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar_url VARCHAR(500) COMMENT '头像URL',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: PENDING, ACTIVE, LOCKED, DISABLED',
    last_login_at DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_phone (phone),
    INDEX idx_email (email),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- -----------------------------------------------------
-- 2. 故事表（官方故事）
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS stories (
    id VARCHAR(64) PRIMARY KEY COMMENT '故事ID',
    title VARCHAR(200) NOT NULL COMMENT '故事标题',
    description VARCHAR(500) NOT NULL COMMENT '故事描述',
    category VARCHAR(50) NOT NULL COMMENT '分类: NATURE, FANTASY, MEDITATION等',
    duration VARCHAR(20) NOT NULL COMMENT '时长: SHORT, MEDIUM, LONG',
    duration_seconds INT COMMENT '音频时长(秒)',
    icon VARCHAR(10) COMMENT '图标',
    gradient_colors VARCHAR(200) COMMENT '渐变色彩',
    audio_url VARCHAR(500) COMMENT '音频文件URL',
    is_generated TINYINT(1) DEFAULT 0 COMMENT '是否AI生成',
    is_favorite TINYINT(1) DEFAULT 0 COMMENT '是否收藏',
    rating FLOAT COMMENT '评分',
    play_count INT DEFAULT 0 COMMENT '播放次数',
    content TEXT COMMENT '故事内容文本',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category (category),
    INDEX idx_is_generated (is_generated),
    INDEX idx_is_favorite (is_favorite),
    INDEX idx_rating (rating),
    INDEX idx_play_count (play_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事表';

-- -----------------------------------------------------
-- 3. 用户资料表
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_profiles (
    user_id VARCHAR(64) PRIMARY KEY COMMENT '用户ID',
    sleep_time VARCHAR(10) DEFAULT '22:30' COMMENT '睡眠时间',
    preferred_duration VARCHAR(20) COMMENT '偏好时长: SHORT, MEDIUM, LONG',
    preferred_categories VARCHAR(200) COMMENT '偏好分类,逗号分隔',
    streak_days INT DEFAULT 0 COMMENT '连续收听天数',
    total_stories_listened INT DEFAULT 0 COMMENT '总收听故事数',
    total_listening_minutes INT DEFAULT 0 COMMENT '总收听分钟数',
    last_listened_at DATETIME COMMENT '最后收听时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户资料表';

-- -----------------------------------------------------
-- 4. 用户收藏表
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_favorites (
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    story_id VARCHAR(64) NOT NULL COMMENT '故事ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (user_id, story_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
    INDEX idx_user_favorites_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';

-- -----------------------------------------------------
-- 5. 用户偏好设置表
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_preferences (
    user_id VARCHAR(64) PRIMARY KEY COMMENT '用户ID',
    dark_mode TINYINT(1) DEFAULT 0 COMMENT '深色模式',
    auto_close_timer INT DEFAULT 30 COMMENT '自动关闭定时器(分钟)',
    volume_level INT DEFAULT 50 COMMENT '音量等级',
    default_play_speed FLOAT DEFAULT 1.0 COMMENT '默认播放速度',
    enable_notifications TINYINT(1) DEFAULT 1 COMMENT '开启通知',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户偏好设置表';

-- -----------------------------------------------------
-- 6. 播放历史表
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS play_history (
    id VARCHAR(64) PRIMARY KEY COMMENT '记录ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    story_id VARCHAR(64) NOT NULL COMMENT '故事ID',
    duration_listened INT NOT NULL DEFAULT 0 COMMENT '收听时长(秒)',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '播放时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
    INDEX idx_play_history_user_id (user_id),
    INDEX idx_play_history_story_id (story_id),
    INDEX idx_play_history_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='播放历史表';

-- -----------------------------------------------------
-- 7. 故事生成任务表
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS story_generation_tasks (
    id VARCHAR(64) PRIMARY KEY COMMENT '任务ID',
    user_id VARCHAR(100) COMMENT '用户ID',
    keywords VARCHAR(500) COMMENT '关键词',
    scene VARCHAR(50) COMMENT '场景: MOONLIGHT, MOUNTAIN, BEACH等',
    mood VARCHAR(50) COMMENT '基调: CALM, WARM, FANTASY等',
    duration VARCHAR(20) NOT NULL COMMENT '时长: SHORT, MEDIUM, LONG',
    status VARCHAR(50) NOT NULL COMMENT '状态: PENDING, PROCESSING, COMPLETED, FAILED',
    story_id VARCHAR(64) COMMENT '生成的故事ID',
    error_message VARCHAR(1000) COMMENT '错误信息',
    progress_percent INT DEFAULT 0 COMMENT '进度百分比',
    status_message VARCHAR(200) COMMENT '状态消息',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    completed_at DATETIME COMMENT '完成时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_story_id (story_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事生成任务表';

-- -----------------------------------------------------
-- 8. 他人创作故事表
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS community_stories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '故事ID',
    user_id VARCHAR(100) NOT NULL COMMENT '创作者用户ID',
    title VARCHAR(200) NOT NULL COMMENT '故事标题',
    summary VARCHAR(500) COMMENT '故事简介/摘要',
    content TEXT NOT NULL COMMENT '故事正文内容',
    voice_config JSON COMMENT '语音配置：{"voiceId": "xxx", "speed": 1.0, "pitch": 1.0}',
    category VARCHAR(50) COMMENT '分类: NATURE, FANTASY, MEDITATION等',
    tags VARCHAR(500) COMMENT '标签，逗号分隔',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-草稿, 1-已发布, 2-已下架',
    likes_count INT DEFAULT 0 COMMENT '点赞数',
    plays_count INT DEFAULT 0 COMMENT '播放次数',
    user_nickname VARCHAR(100) COMMENT '创作者昵称',
    user_avatar VARCHAR(500) COMMENT '创作者头像URL',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_category (category),
    INDEX idx_created_at (created_at),
    INDEX idx_likes_count (likes_count),
    INDEX idx_plays_count (plays_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='他人创作故事表';

-- -----------------------------------------------------
-- 9. 故事点赞表
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS story_likes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id VARCHAR(100) NOT NULL COMMENT '用户ID',
    story_id BIGINT NOT NULL COMMENT '故事ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    UNIQUE KEY uk_user_story (user_id, story_id),
    INDEX idx_story_id (story_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事点赞表';

-- -----------------------------------------------------
-- 插入示例故事数据
-- -----------------------------------------------------
INSERT INTO stories (id, title, description, category, duration, duration_seconds, icon, gradient_colors, audio_url, is_generated, is_favorite, rating, play_count, content, created_at, updated_at) VALUES
('story-001', '月光森林', '在宁静的月光下，漫步在神秘的森林中，聆听大自然的声音', 'NATURE', 'MEDIUM', 600, '🌲', '#1a237e,#4a148c', 'https://example.com/audio/forest.mp3', 0, 0, 4.8, 1250, '夜幕降临，月光如水般洒在森林中...', NOW(), NOW()),
('story-002', '星空下的海滩', '躺在柔软的沙滩上，仰望满天繁星，感受海浪的轻抚', 'NATURE', 'SHORT', 300, '🏖️', '#0d47a1,#1565c0', 'https://example.com/audio/beach.mp3', 0, 0, 4.9, 980, '海浪轻轻拍打着沙滩，发出有节奏的声响...', NOW(), NOW()),
('story-003', '云端城堡', '漂浮在云端之上的神秘城堡，带你进入一个奇幻的梦境世界', 'FANTASY', 'LONG', 900, '🏰', '#7c4dff,#b388ff', 'https://example.com/audio/castle.mp3', 0, 0, 4.7, 756, '在云层之上，有一座晶莹剔透的城堡...', NOW(), NOW()),
('story-004', '正念呼吸', '跟随引导进行深呼吸练习，放松身心，进入深度睡眠', 'MEDITATION', 'SHORT', 180, '🧘', '#00695c,#00897b', 'https://example.com/audio/breathing.mp3', 0, 0, 4.6, 543, '请找一个舒适的姿势躺下...', NOW(), NOW()),
('story-005', '雨夜小屋', '窗外下着细雨，你在温暖的小屋里，听着雨声安然入睡', 'NATURE', 'MEDIUM', 720, '🏠', '#263238,#37474f', 'https://example.com/audio/rain.mp3', 0, 1, 4.9, 2100, '雨滴敲打着窗户，发出轻柔的声响...', NOW(), NOW());
