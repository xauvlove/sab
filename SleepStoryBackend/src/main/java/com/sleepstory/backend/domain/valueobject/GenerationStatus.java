package com.sleepstory.backend.domain.valueobject;

import lombok.Getter;

@Getter
public enum GenerationStatus {
    PENDING("待处理", 0),
    PROCESSING("处理中", 50),
    COMPLETED("已完成", 100),
    FAILED("失败", -1);

    private final String displayName;
    private final int defaultProgress;

    GenerationStatus(String displayName, int defaultProgress) {
        this.displayName = displayName;
        this.defaultProgress = defaultProgress;
    }

    public boolean isFinished() {
        return this == COMPLETED || this == FAILED;
    }

    public boolean isProcessing() {
        return this == PROCESSING;
    }
}
