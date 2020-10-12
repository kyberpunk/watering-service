package com.kyberpunk.iot.watering.task;

import com.kyberpunk.iot.watering.service.DevicesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WateringTask {
    private final DevicesService devicesService;

    public WateringTask(DevicesService devicesService) {
        this.devicesService = devicesService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void scanDevicesState() {
        devicesService.updateDevices();
    }

    @Scheduled(cron = "0 * * * * *")
    public void processSchedules() {

    }
}
