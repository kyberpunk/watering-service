package com.kyberpunk.iot.watering.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WateringTask {
    @Scheduled(cron = "0 * * * * *")
    public void scanDevicesState() {

    }

    @Scheduled(cron = "0 * * * * *")
    public void processSchedules() {

    }
}
