package com.sleepstory.backend.domain.valueobject;

import lombok.Getter;

/**
 * 性别值对象
 */
@Getter
public enum Gender {
    UNKNOWN("未知", 0),
    MALE("男", 1),
    FEMALE("女", 2),
    OTHER("其他", 3);

    private final String displayName;
    private final int value;

    Gender(String displayName, int value) {
        this.displayName = displayName;
        this.value = value;
    }
}
