package com.kyberpunk.iot.watering.service;

import com.kyberpunk.iot.watering.client.DeviceApiClient;
import com.kyberpunk.iot.watering.client.DeviceInfo;
import com.kyberpunk.iot.watering.dto.WateringDto;
import com.kyberpunk.iot.watering.repository.DeviceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
public class WateringService {
    private final DeviceApiClient client;
    private final DeviceRepository deviceRepository;

    public WateringService(DeviceApiClient client, DeviceRepository deviceRepository) {
        this.client = client;
        this.deviceRepository = deviceRepository;
    }

    public boolean waterNow(WateringDto watering) {
        var device = deviceRepository.findById(watering.getDeviceId()).orElseThrow();
        device.setLastWatered(LocalDateTime.now());
        try {
            DeviceInfo info = client.executeSwitchCommand(device.getIp(), true, watering.getDuration() * 1000).get();
            if (info.isSwitchedOn())
                deviceRepository.save(device);
        } catch (Exception e) {
            log.warn("Execution of watering failed.", e);
            return false;
        }
        return true;
    }
}
