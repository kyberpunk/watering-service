package com.kyberpunk.iot.watering.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Device {
    @Id
    private String deviceId;
    @Column(nullable = false)
    private String ip;
    @Column(nullable = false)
    private String description;
}
