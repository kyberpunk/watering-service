package com.kyberpunk.iot.watering.model;

import lombok.Data;

import java.util.Date;

@Data
public class Device {
    private String deviceId;
    private boolean switchedOn;
    private Integer timeout;
    private Date lastChange;
}
