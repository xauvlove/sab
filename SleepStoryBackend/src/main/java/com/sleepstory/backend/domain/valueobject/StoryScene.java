package com.sleepstory.backend.domain.valueobject;

import lombok.Getter;

@Getter
public enum StoryScene {
    MOONLIGHT("月夜", "🌙", "宁静的月光下"),
    MOUNTAIN("山间", "🏔️", "清新的山林中"),
    BEACH("海边", "🏖️", "温柔的海浪旁"),
    COTTAGE("小屋", "🏡", "温馨的小屋里"),
    JOURNEY("旅途", "🚂", "奇妙的旅途中"),
    GARDEN("花园", "🌸", "芬芳的花园中");

    private final String displayName;
    private final String icon;
    private final String atmosphere;

    StoryScene(String displayName, String icon, String atmosphere) {
        this.displayName = displayName;
        this.icon = icon;
        this.atmosphere = atmosphere;
    }
}
