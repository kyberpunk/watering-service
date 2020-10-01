package com.kyberpunk.iot.watering.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private boolean active;
    private String description;
    @ManyToOne(optional = false)
    private Device device;
    @Column(name="intervalValue")
    private int interval;
    private TimeUnit unit;
    private int duration;
    @Column(nullable = false)
    private LocalDateTime startAt;
    private LocalDateTime lastWatered;
}
