package com.sleepstory.backend.domain.valueobject;

import lombok.Getter;

@Getter
public enum StoryDuration {
    SHORT("短篇", 5, 10, "5-10分钟"),
    MEDIUM("中篇", 15, 30, "15-30分钟"),
    LONG("长篇", 30, 60, "30-60分钟");

    private final String displayName;
    private final int minMinutes;
    private final int maxMinutes;
    private final String description;

    StoryDuration(String displayName, int minMinutes, int maxMinutes, String description) {
        this.displayName = displayName;
        this.minMinutes = minMinutes;
        this.maxMinutes = maxMinutes;
        this.description = description;
    }

    public int getTargetWordCount() {
        int avgMinutes = (minMinutes + maxMinutes) / 2;
        return avgMinutes * 150;
    }

    public int getTargetDurationSeconds() {
        int avgMinutes = (minMinutes + maxMinutes) / 2;
        return avgMinutes * 60;
    }
}
