package com.kyberpunk.iot.watering.repository;

import com.kyberpunk.iot.watering.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {
}
