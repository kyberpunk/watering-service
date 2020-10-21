package com.kyberpunk.iot.watering.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceInfo {
    @JsonProperty("id")
    private String deviceId;
    private boolean switchedOn;
    private int timeout;
    @JsonProperty("lastChangeUtcMillis")
    private Date lastChange;
}
