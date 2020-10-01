package com.kyberpunk.iot.watering.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class Device {
    @Id
    private String deviceId;
    @Column(nullable = false)
    private String ip;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private DeviceStatus status;
    private LocalDateTime lastWatered;
}
