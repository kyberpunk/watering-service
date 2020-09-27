package com.kyberpunk.iot.watering.dto;

import lombok.Data;

@Data
public class DeviceDto {
    private String deviceId;
    private String ip;
    private String description;
}
