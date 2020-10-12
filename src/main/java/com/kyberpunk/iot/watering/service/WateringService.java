package com.kyberpunk.iot.watering.service;

import com.kyberpunk.iot.watering.client.DeviceApiClient;
import com.kyberpunk.iot.watering.dto.WateringDto;
import com.kyberpunk.iot.watering.repository.DeviceRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
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
        try {
            client.executeSwitchCommand(device.getIp(), true, watering.getDuration() * 1000).get();
        } catch (Exception e) {
            log.warn("Execution of watering failed.", e);
            return false;
        }
        device.setLastWatered(LocalDateTime.now());
        deviceRepository.save(device);
        return true;
    }
}
