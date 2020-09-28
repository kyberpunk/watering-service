package com.kyberpunk.iot.watering.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class NewDeviceDto {
    @NotEmpty
    private String deviceId;
    @Pattern(regexp = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$", message = "Provide valid IPv4 address.")
    private String ip;
    @NotNull
    private String description;
}
