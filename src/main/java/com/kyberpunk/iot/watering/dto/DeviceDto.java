package com.kyberpunk.iot.watering.dto;

import lombok.Data;

@Data
public class DeviceDto {
    private String Id;
    private boolean swicthedOn;
    private Integer timeout;
    private Integer lastChangeUtcMillis;
}
