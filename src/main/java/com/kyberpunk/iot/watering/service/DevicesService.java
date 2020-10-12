package com.kyberpunk.iot.watering.service;

import com.kyberpunk.iot.watering.client.DeviceApiClient;
import com.kyberpunk.iot.watering.dto.DeviceDto;
import com.kyberpunk.iot.watering.dto.NewDeviceDto;
import com.kyberpunk.iot.watering.mapper.DeviceMapper;
import com.kyberpunk.iot.watering.model.Device;
import com.kyberpunk.iot.watering.model.DeviceStatus;
import com.kyberpunk.iot.watering.repository.DeviceRepository;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class DevicesService {
    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final DeviceApiClient deviceClient;

    public DevicesService(DeviceRepository deviceRepository, DeviceMapper deviceMapper, DeviceApiClient deviceClient) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
        this.deviceClient = deviceClient;
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

    @SneakyThrows
    public void updateDevices() {
        var devices = deviceRepository.findAll();
        var futures = devices.stream()
                .map(this::createStateFuture)
                .collect(Collectors.toList());
        for (var future : futures)
            future.get();
        deviceRepository.saveAll(devices);
    }

    private CompletableFuture createStateFuture(Device device) {
        return deviceClient.getState(device.getIp())
                .thenAccept(i -> device.setStatus(i != null ? DeviceStatus.ONLINE : DeviceStatus.OFFLINE))
                .exceptionally(e -> {
                    device.setStatus(DeviceStatus.OFFLINE);
                    return null;
                });
    }

    public void delete(String deviceId) {
        deviceRepository.deleteById(deviceId);
    }
}
