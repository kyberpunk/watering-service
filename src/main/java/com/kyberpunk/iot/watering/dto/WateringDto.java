package com.kyberpunk.iot.watering.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class WateringDto {
    @NotEmpty(message = "Any device must be selected.")
    private String deviceId;
    @Max(value = 30, message = "Duration must be less than 30 s.")
    @Min(value = 1, message = "Duration must be greater than 1.")
    private int duration;
}
