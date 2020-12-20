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

package com.kyberpunk.iot.watering.service;

import com.kyberpunk.iot.watering.dto.ScheduleDto;
import com.kyberpunk.iot.watering.mapper.DeviceMapper;
import com.kyberpunk.iot.watering.mapper.ScheduleMapper;
import com.kyberpunk.iot.watering.model.Schedule;
import com.kyberpunk.iot.watering.repository.DeviceRepository;
import com.kyberpunk.iot.watering.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * This service class implements methods for managing watering schedules resource in the persistent database.
 */
@Service
public class SchedulesService {
    private final ScheduleRepository scheduleRepository;
    private final DeviceRepository deviceRepository;
    private final ScheduleMapper scheduleMapper;
    private final DeviceMapper deviceMapper;

    /**
     * Parametric constructor for dependency injection.
     * @param scheduleRepository {@link ScheduleRepository} instance.
     * @param deviceRepository {@link DeviceRepository} instance.
     * @param scheduleMapper {@link ScheduleMapper} instance.
     * @param deviceMapper {@link DeviceMapper} instance.
     */
    public SchedulesService(ScheduleRepository scheduleRepository, DeviceRepository deviceRepository,
                            ScheduleMapper scheduleMapper, DeviceMapper deviceMapper) {
        this.scheduleRepository = scheduleRepository;
        this.deviceRepository = deviceRepository;
        this.scheduleMapper = scheduleMapper;
        this.deviceMapper = deviceMapper;
    }

    /**
     * Find all persisted schedules.
     * @return Returns list of schedules. It is empty if no schedule was found.
     */
    public List<ScheduleDto> findAll() {
        var schedules = scheduleRepository.findAll();
        return entityToDto(schedules);
    }

    /**
     * Find a schedule by ID. {@link NoSuchElementException} is thrown when schedule with given id does not exist.
     * @param id ID of the schedule.
     * @return Returns schedule with the given ID if exists.
     */
    public ScheduleDto findById(Long id) {
        var schedule = scheduleRepository.findById(id).orElseThrow();
        return entityToDto(schedule);
    }

    /**
     * Persist new schedule in database.
     * @param dto Schedule to be persisted.
     * @return Returns created schedule with corresponding ID.
     */
    public ScheduleDto create(ScheduleDto dto) {
        var schedule = scheduleMapper.dtoToEntity(dto);
        var device = deviceRepository.findById(dto.getDeviceId()).orElseThrow();
        schedule.setDevice(device);
        schedule.setLastWatered(null);
        schedule.setActive(true);
        var created = scheduleRepository.save(schedule);
        return entityToDto(created);
    }

    /**
     * Update persisted schedule data.
     * @param dto Schedule to be updated.
     * @return Returns updated schedule.
     */
    public ScheduleDto update(ScheduleDto dto) {
        var schedule = scheduleMapper.dtoToEntity(dto);
        var originalSchedule = scheduleRepository.findById(schedule.getId()).orElseThrow();
        var device = deviceRepository.findById(dto.getDeviceId()).orElseThrow();
        schedule.setDevice(device);
        schedule.setActive(originalSchedule.isActive());
        schedule.setLastWatered(originalSchedule.getLastWatered());
        var created = scheduleRepository.save(schedule);
        return entityToDto(created);
    }

    /**
     * Enable schedule with the given ID. Schedule will be executed when scheduled time elapses.
     * If the last scheduled watering was not executed, then it will be executed in the next minute.
     * @param id ID of the schedule to be enabled.
     */
    public void enable(Long id) {
        setActiveAndSave(id, true);
    }

    /**
     * Disable schedule with the given ID. Schedule won't be executed until it will be enabled again.
     * @param id ID of the schedule to be disabled.
     */
    public void disable(Long id) {
        setActiveAndSave(id, false);
    }

    private void setActiveAndSave(Long id, boolean value) {
        var schedule = scheduleRepository.findById(id).orElseThrow();
        schedule.setActive(value);
        scheduleRepository.save(schedule);
    }

    /**
     * Set last watered time of the schedule to current time.
     * @param id Schedule ID.
     */
    public void setLastWateredNow(Long id) {
        var schedule = scheduleRepository.findById(id).orElseThrow();
        schedule.setLastWatered(LocalDateTime.now());
        scheduleRepository.save(schedule);
    }

    /**
     * Delete schedule with the given ID.
     * @param id ID of the schedule to be deleted.
     */
    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

    private ScheduleDto entityToDto(Schedule schedule) {
        var scheduleDto = scheduleMapper.entityToDto(schedule);
        var deviceDto = deviceMapper.entityToDto(schedule.getDevice());
        scheduleDto.setDeviceDisplayValue(deviceDto.getDisplayValue());
        return scheduleDto;
    }

    private List<ScheduleDto> entityToDto(List<Schedule> schedules) {
        return schedules.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
