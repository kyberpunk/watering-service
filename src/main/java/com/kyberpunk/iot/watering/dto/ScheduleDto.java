package com.kyberpunk.iot.watering.dto;

import com.kyberpunk.iot.watering.model.TimeUnit;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
public class ScheduleDto {
    private long id;
    private boolean active;
    private String description;
    private String deviceId;
    private String deviceDisplayValue;
    @Min(value = 1, message = "Interval must be greater than 1.")
    private int interval;
    private TimeUnit unit;
    @Max(value = 30, message = "Duration must be less than 30 s.")
    @Min(value = 1, message = "Duration must be greater than 1.")
    private int duration;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startAt;
    private LocalDateTime lastWatered;
}
