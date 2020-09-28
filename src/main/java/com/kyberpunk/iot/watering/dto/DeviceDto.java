package com.kyberpunk.iot.watering.dto;

import com.kyberpunk.iot.watering.model.DeviceStatus;
import lombok.Data;

@Data
public class DeviceDto {
    private String deviceId;
    private String ip;
    private String description;
    private DeviceStatus status;
}
