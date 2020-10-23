package com.kyberpunk.iot.watering.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @OneToMany(targetEntity = Schedule.class, mappedBy = "device", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Schedule> schedules;
}
