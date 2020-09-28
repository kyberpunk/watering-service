package com.kyberpunk.iot.watering.service;

import com.kyberpunk.iot.watering.dto.DeviceDto;
import com.kyberpunk.iot.watering.dto.NewDeviceDto;
import com.kyberpunk.iot.watering.mapper.DeviceMapper;
import com.kyberpunk.iot.watering.model.Device;
import com.kyberpunk.iot.watering.model.DeviceStatus;
import com.kyberpunk.iot.watering.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevicesService {
    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DevicesService(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    public List<DeviceDto> findAll() {
        var devices = deviceRepository.findAll();
        return deviceMapper.entityToDto(devices);
    }

    public DeviceDto create(NewDeviceDto dto) {
        var device = deviceMapper.dtoToEntity(dto);
        device.setStatus(DeviceStatus.OFFLINE);
        var created = deviceRepository.save(device);
        return deviceMapper.entityToDto(created);
    }

    public void delete(String deviceId) {
        deviceRepository.deleteById(deviceId);
    }
}
