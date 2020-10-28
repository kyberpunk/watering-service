package com.kyberpunk.iot.watering.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Getter
public enum TimeUnit {
    MINUTES(ChronoUnit.MINUTES),
    HOURS(ChronoUnit.HOURS),
    DAYS(ChronoUnit.DAYS);

    private final TemporalUnit temporalUnit;

    TimeUnit(TemporalUnit temporalUnit) {
        this.temporalUnit = temporalUnit;
    }

    public static LocalDateTime add(LocalDateTime dateTime, int amount, TimeUnit unit) {
        return dateTime.plus(amount, unit.getTemporalUnit());
    }
}
