package com.kyberpunk.iot.watering.task;

import com.kyberpunk.iot.watering.client.DeviceApiClient;
import com.kyberpunk.iot.watering.model.Schedule;
import com.kyberpunk.iot.watering.repository.ScheduleRepository;
import com.kyberpunk.iot.watering.service.DevicesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Log4j2
@Component
public class WateringTask {
    private final DevicesService devicesService;
    private final ScheduleRepository scheduleRepository;
    private final DeviceApiClient deviceApiClient;

    public WateringTask(DevicesService devicesService, ScheduleRepository scheduleRepository, DeviceApiClient deviceApiClient) {
        this.devicesService = devicesService;
        this.scheduleRepository = scheduleRepository;
        this.deviceApiClient = deviceApiClient;
    }

    @Scheduled(cron = "0 * * * * *")
    public void scanDevicesState() {
        devicesService.updateDevices();
    }

    @Scheduled(cron = "0 * * * * *")
    public void processSchedules() {
        LocalDateTime now = LocalDateTime.now().withSecond(0);
        var schedulesToExecute = scheduleRepository.findAll().stream()
                .filter(s -> isWateringNeeded(s, now))
                .collect(Collectors.toList());
        var executedFutures = schedulesToExecute.stream()
                .map(s -> execute(s, now))
                .collect(Collectors.toList());
        waitForFutures(executedFutures);
        scheduleRepository.saveAll(schedulesToExecute);
    }

    private boolean isWateringNeeded(Schedule schedule, LocalDateTime now) {
        LocalDateTime nextWatering = schedule.getLastWatered()
                .withSecond(0)
                .plus(schedule.getInterval(), schedule.getUnit().getTemporalUnit());
        return schedule.getLastWatered().isBefore(nextWatering);
    }

    private CompletableFuture<Void> execute(Schedule schedule, LocalDateTime now) {
        String ip = schedule.getDevice().getIp();
        int durationMillis = schedule.getDuration() * 1000;
        return deviceApiClient.executeSwitchCommand(ip, true, durationMillis)
                .thenAccept(success -> {
                    if (success) {
                        schedule.setLastWatered(now);
                        schedule.getDevice().setLastWatered(now);
                    }
                });
    }

    private void waitForFutures(List<CompletableFuture<Void>> futures) {
        for (var future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                log.warn("Execution of schedule failed.", e);
            }
        }
    }
}
