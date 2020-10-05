package com.kyberpunk.iot.watering.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Getter
public enum TimeUnit {
    MINUTES("minutes", ChronoUnit.MINUTES),
    HOURS("hours", ChronoUnit.HOURS),
    DAYS("days", ChronoUnit.DAYS);

    private final String displayValue;
    private final TemporalUnit temporalUnit;

    TimeUnit(String displayValue, TemporalUnit temporalUnit) {
        this.displayValue = displayValue;
        this.temporalUnit = temporalUnit;
    }

    public static LocalDateTime add(LocalDateTime dateTime, int amount, TimeUnit unit) {
        return dateTime.plus(amount, unit.getTemporalUnit());
    }
}
