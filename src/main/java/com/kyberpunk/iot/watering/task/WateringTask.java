/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, Vit Holasek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

/**
 * This class implements scheduled tasks used for execution of periodic jobs which are needed for watering.
 */
@Log4j2
@Component
public class WateringTask {
    private final DevicesService devicesService;
    private final ScheduleRepository scheduleRepository;
    private final DeviceApiClient deviceApiClient;

    /**
     * Parametric constructor for dependency injection.
     * @param devicesService {@link DevicesService} instance.
     * @param scheduleRepository {@link ScheduleRepository} instance.
     * @param deviceApiClient {@link DeviceApiClient} instance.
     */
    public WateringTask(DevicesService devicesService, ScheduleRepository scheduleRepository,
                        DeviceApiClient deviceApiClient) {
        this.devicesService = devicesService;
        this.scheduleRepository = scheduleRepository;
        this.deviceApiClient = deviceApiClient;
    }

    /**
     * This method scans all registered devices and updates its states. It is called every minute.
     */
    @Scheduled(cron = "0 * * * * *")
    public void scanDevicesState() {
        devicesService.updateDevices();
    }

    /**
     * This method checks all schedules and fires appropriate actions. It is called every minute.
     */
    @Scheduled(cron = "0 * * * * *")
    public void processSchedules() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        log.debug("Processing schedules: " + now);
        var schedulesToExecute = scheduleRepository.findAll().stream()
                .filter(Schedule::isActive)
                .filter(s -> isWateringNeeded(s, now))
                .collect(Collectors.toList());
        var executedFutures = schedulesToExecute.stream()
                .map(s -> executeSchedule(s, now))
                .collect(Collectors.toList());
        waitForFutures(executedFutures);
        scheduleRepository.saveAll(schedulesToExecute);
    }

    private boolean isWateringNeeded(Schedule schedule, LocalDateTime now) {
        // Check if schedule started
        if (now.isBefore(schedule.getStartAt()))
            return false;
        // If not watered yet then water immediately
        if (schedule.getLastWatered() == null)
            return true;
        log.debug("Schedule: " + schedule);

        LocalDateTime nextWatering = schedule.getLastWatered()
                .withSecond(0).withNano(0)
                .plus(schedule.getInterval(), schedule.getUnit().getTemporalUnit());
        return now.isEqual(nextWatering) || now.isAfter(nextWatering);
    }

    /**
     * Execute action of the single schedule.
     * @param schedule Schedule which has to be executed.
     * @param now Current time.
     * @return Returns completable future which is finished when the action is successfully executed.
     */
    private CompletableFuture<Void> executeSchedule(Schedule schedule, LocalDateTime now) {
        String ip = schedule.getDevice().getIp();
        int durationMillis = schedule.getDuration() * 1000;
        return deviceApiClient.executeSwitchCommand(ip, true, durationMillis)
            .thenAccept(info -> {
                // Actualize device status when it was successfully switched on.
                if (info.isSwitchedOn()) {
                    schedule.setLastWatered(now);
                    schedule.getDevice().setLastWatered(now);
                }
            }).exceptionally(e -> {
                log.debug("Schedule request failed.", e);
                return null;
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
