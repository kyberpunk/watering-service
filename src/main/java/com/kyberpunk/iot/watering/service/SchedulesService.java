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
import java.util.stream.Collectors;

@Service
public class SchedulesService {
    private final ScheduleRepository scheduleRepository;
    private final DeviceRepository deviceRepository;
    private final ScheduleMapper scheduleMapper;
    private final DeviceMapper deviceMapper;

    public SchedulesService(ScheduleRepository scheduleRepository, DeviceRepository deviceRepository, ScheduleMapper scheduleMapper, DeviceMapper deviceMapper) {
        this.scheduleRepository = scheduleRepository;
        this.deviceRepository = deviceRepository;
        this.scheduleMapper = scheduleMapper;
        this.deviceMapper = deviceMapper;
    }

    public List<ScheduleDto> findAll() {
        var schedules = scheduleRepository.findAll();
        return entityToDto(schedules);
    }

    public ScheduleDto findById(Long id) {
        var schedule = scheduleRepository.findById(id).orElseThrow();
        return entityToDto(schedule);
    }

    public ScheduleDto create(ScheduleDto dto) {
        dto.setLastWatered(null);
        dto.setActive(true);
        return update(dto);
    }

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

    public void enable(Long id) {
        setActiveAndSave(id, true);
    }

    public void disable(Long id) {
        setActiveAndSave(id, false);
    }

    private void setActiveAndSave(Long id, boolean value) {
        var schedule = scheduleRepository.findById(id).orElseThrow();
        schedule.setActive(value);
        scheduleRepository.save(schedule);
    }

    public void setLastWateredNow(Long id) {
        var schedule = scheduleRepository.findById(id).orElseThrow();
        schedule.setLastWatered(LocalDateTime.now());
        scheduleRepository.save(schedule);
    }

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
