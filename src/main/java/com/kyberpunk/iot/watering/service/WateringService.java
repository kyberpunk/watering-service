package com.kyberpunk.iot.watering.service;

import com.kyberpunk.iot.watering.client.DeviceApiClient;
import com.kyberpunk.iot.watering.dto.WateringDto;
import com.kyberpunk.iot.watering.repository.DeviceRepository;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WateringService {
    private final DeviceApiClient client;
    private final DeviceRepository deviceRepository;

    public WateringService(DeviceApiClient client, DeviceRepository deviceRepository) {
        this.client = client;
        this.deviceRepository = deviceRepository;
    }

    public void waterNow(WateringDto watering) {
        var device = deviceRepository.findById(watering.getDeviceId()).orElseThrow();
        client.executeSwitchCommand(device.getIp(), true, watering.getDuration() * 1000);
        device.setLastWatered(LocalDateTime.now());
        deviceRepository.save(device);
    }
}
