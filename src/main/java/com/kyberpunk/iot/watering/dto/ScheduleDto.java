package com.kyberpunk.iot.watering.dto;

import com.kyberpunk.iot.watering.model.TimeUnit;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class ScheduleDto {
    private long id;
    private boolean active;
    @NotEmpty(message = "{validation.description_empty}")
    private String description;
    @NotEmpty(message = "{validation.device_empty}")
    private String deviceId;
    private String deviceDisplayValue;
    @Min(value = 1, message = "{validation.interval_min}")
    private int interval;
    private TimeUnit unit;
    @Max(value = 30, message = "{validation.duration_max}")
    @Min(value = 1, message = "{validation.duration_min}")
    private int duration;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startAt;
    private LocalDateTime lastWatered;
}
