package com.kyberpunk.iot.watering.model;

import lombok.Getter;

@Getter
public enum TimeUnit {
    MINUTES("minutes"),
    HOURS("hours"),
    DAYS("days");

    private final String displayValue;

    TimeUnit(String displayValue) {
        this.displayValue = displayValue;
    }
}
