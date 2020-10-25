package com.kyberpunk.iot.watering.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class WateringDto {
    @NotEmpty(message = "{validation.device_empty}")
    private String deviceId;
    @Max(value = 30, message = "{validation.duration_max}")
    @Min(value = 1, message = "{validation.duration_min}")
    private int duration;
}
