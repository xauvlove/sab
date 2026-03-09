-- H2 数据库初始化脚本（用于开发环境）

-- 故事表
CREATE TABLE IF NOT EXISTS stories (
    id VARCHAR(64) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(500) NOT NULL,
    category VARCHAR(50) NOT NULL,
    duration VARCHAR(20) NOT NULL,
    duration_seconds INT,
    icon VARCHAR(10),
    gradient_colors VARCHAR(200),
    audio_url VARCHAR(500),
    is_generated BOOLEAN DEFAULT FALSE,
    is_favorite BOOLEAN DEFAULT FALSE,
    rating FLOAT,
    play_count INT DEFAULT 0,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 故事生成任务表
CREATE TABLE IF NOT EXISTS story_generation_tasks (
    id VARCHAR(64) PRIMARY KEY,
    user_id VARCHAR(100),
    keywords VARCHAR(500),
    scene VARCHAR(50),
    mood VARCHAR(50),
    duration VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL,
    story_id VARCHAR(64),
    error_message VARCHAR(1000),
    progress_percent INT DEFAULT 0,
    status_message VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(64) PRIMARY KEY,
    phone VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    last_login_at TIMESTAMP,
    last_login_ip VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 用户资料表
CREATE TABLE IF NOT EXISTS user_profiles (
    user_id VARCHAR(64) PRIMARY KEY,
    sleep_time VARCHAR(10) DEFAULT '22:30',
    preferred_duration VARCHAR(20),
    preferred_categories VARCHAR(200),
    streak_days INT DEFAULT 0,
    total_stories_listened INT DEFAULT 0,
    total_listening_minutes INT DEFAULT 0,
    last_listened_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_stories_category ON stories(category);
CREATE INDEX IF NOT EXISTS idx_stories_is_generated ON stories(is_generated);
CREATE INDEX IF NOT EXISTS idx_stories_is_favorite ON stories(is_favorite);
CREATE INDEX IF NOT EXISTS idx_tasks_user_id ON story_generation_tasks(user_id);
CREATE INDEX IF NOT EXISTS idx_tasks_status ON story_generation_tasks(status);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);

-- 插入示例数据
INSERT INTO stories (id, title, description, category, duration, duration_seconds, icon, gradient_colors, audio_url, is_generated, is_favorite, rating, play_count, content) VALUES
('story-001', '月光森林', '在宁静的月光下，漫步在神秘的森林中，聆听大自然的声音', 'NATURE', 'MEDIUM', 600, '🌲', '#1a237e,#4a148c', 'https://example.com/audio/forest.mp3', false, false, 4.8, 1250, '夜幕降临，月光如水般洒在森林中...'),
('story-002', '星空下的海滩', '躺在柔软的沙滩上，仰望满天繁星，感受海浪的轻抚', 'NATURE', 'SHORT', 300, '🏖️', '#0d47a1,#1565c0', 'https://example.com/audio/beach.mp3', false, false, 4.9, 980, '海浪轻轻拍打着沙滩，发出有节奏的声响...'),
('story-003', '云端城堡', '漂浮在云端之上的神秘城堡，带你进入一个奇幻的梦境世界', 'FANTASY', 'LONG', 900, '🏰', '#7c4dff,#b388ff', 'https://example.com/audio/castle.mp3', false, false, 4.7, 756, '在云层之上，有一座晶莹剔透的城堡...'),
('story-004', '正念呼吸', '跟随引导进行深呼吸练习，放松身心，进入深度睡眠', 'MEDITATION', 'SHORT', 180, '🧘', '#00695c,#00897b', 'https://example.com/audio/breathing.mp3', false, false, 4.6, 543, '请找一个舒适的姿势躺下...'),
('story-005', '雨夜小屋', '窗外下着细雨，你在温暖的小屋里，听着雨声安然入睡', 'NATURE', 'MEDIUM', 720, '🏠', '#263238,#37474f', 'https://example.com/audio/rain.mp3', false, true, 4.9, 2100, '雨滴敲打着窗户，发出轻柔的声响...');
