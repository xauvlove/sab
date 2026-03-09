package com.sleepstory.backend.domain.valueobject;

import lombok.Getter;

@Getter
public enum StoryCategory {
    NATURE("自然探索", "🌲", "森林、海洋、山川"),
    FANTASY("奇幻冒险", "🏰", "魔法、传说、异世界"),
    MEDITATION("冥想疗愈", "🧘", "正念、放松、治愈"),
    SCIFI("科幻未来", "🚀", "太空、科技、未来"),
    CLASSIC("经典文学", "📚", "名著、诗歌、散文"),
    WARM("温暖治愈", "💕", "爱情、友情、日常");

    private final String displayName;
    private final String icon;
    private final String description;

    StoryCategory(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
}
