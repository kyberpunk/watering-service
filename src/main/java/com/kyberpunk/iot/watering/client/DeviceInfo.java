package com.kyberpunk.iot.watering.client;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceInfo {
    private String deviceId;
    private boolean switchedOn;
    private Integer timeout;
    private Date lastChange;
}
