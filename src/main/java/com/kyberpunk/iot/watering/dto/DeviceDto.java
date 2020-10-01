package com.kyberpunk.iot.watering.dto;

import com.kyberpunk.iot.watering.model.DeviceStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceDto {
    private String deviceId;
    private String ip;
    private String description;
    private DeviceStatus status;
    private LocalDateTime lastWatered;

    public String getDisplayValue() {
        return description == null || description.isBlank() ? deviceId : description;
    }
}
