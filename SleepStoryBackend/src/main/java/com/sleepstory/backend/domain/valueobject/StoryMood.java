package com.sleepstory.backend.domain.valueobject;

import lombok.Getter;

@Getter
public enum StoryMood {
    CALM(0.0, "平静", "舒缓、安静、放松"),
    WARM(0.5, "温暖", "温馨、舒适、治愈"),
    FANTASY(1.0, "奇幻", "神秘、梦幻、想象");

    private final double value;
    private final String displayName;
    private final String description;

    StoryMood(double value, String displayName, String description) {
        this.value = value;
        this.displayName = displayName;
        this.description = description;
    }

    public static StoryMood fromValue(double value) {
        if (value < 0.33) {
            return CALM;
        } else if (value < 0.66) {
            return WARM;
        } else {
            return FANTASY;
        }
    }
}
