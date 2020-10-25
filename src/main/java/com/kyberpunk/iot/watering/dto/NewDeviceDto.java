package com.kyberpunk.iot.watering.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewDeviceDto {
    @NotEmpty(message = "{validation.device_id_empty}")
    private String deviceId;
    @NotEmpty(message = "{validation.ip_empty}")
    private String ip;
    @NotNull
    private String description;
}
