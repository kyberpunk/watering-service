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

import com.kyberpunk.iot.watering.client.DeviceApiClient;
import com.kyberpunk.iot.watering.dto.DeviceDto;
import com.kyberpunk.iot.watering.dto.NewDeviceDto;
import com.kyberpunk.iot.watering.mapper.DeviceMapper;
import com.kyberpunk.iot.watering.model.Device;
import com.kyberpunk.iot.watering.model.DeviceStatus;
import com.kyberpunk.iot.watering.repository.DeviceRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This service class implements methods for managing devices persisted in the database.
 */
@Service
public class DevicesService {
    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final DeviceApiClient deviceClient;

    /**
     * Parametric constructor for dependency injection.
     * @param deviceRepository {@link DeviceRepository} instance.
     * @param deviceMapper {@link DeviceMapper} instance.
     * @param deviceClient {@link DeviceApiClient} instance.
     */
    public DevicesService(DeviceRepository deviceRepository, DeviceMapper deviceMapper,
                          DeviceApiClient deviceClient) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
        this.deviceClient = deviceClient;
    }

    /**
     * Find all persisted watering devices.
     * @return Returns list of devices. It is empty if no device was found.
     */
    public List<DeviceDto> findAll() {
        var devices = deviceRepository.findAll();
        return deviceMapper.entityToDto(devices);
    }

    /**
     * Find a device by the given ID.
     * @param deviceId Watering device ID.
     * @return Returns {@link Optional} instance with the device. It is empty if device with the given ID was not found.
     */
    public Optional<DeviceDto> findById(String deviceId) {
        var optionalDevice = deviceRepository.findById(deviceId);
        if (optionalDevice.isPresent()) {
            DeviceDto dto = deviceMapper.entityToDto(optionalDevice.get());
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Persist new device. Device online state is updated immediately. Device state is set to online if it is reachable
     * on the network.
     * @param dto New device to be persisted.
     * @return Returns created device with corresponding ID.
     */
    @SneakyThrows
    public DeviceDto create(NewDeviceDto dto) {
        var device = deviceMapper.dtoToEntity(dto);
        device.setStatus(DeviceStatus.OFFLINE);
        updateDeviceState(device).get();
        var created = deviceRepository.save(device);
        return deviceMapper.entityToDto(created);
    }

    /**
     * Update online states of all persisted devices. Device state is set to online if it is reachable on the network.
     */
    @SneakyThrows
    public void updateDevices() {
        var devices = deviceRepository.findAll();
        var futures = devices.stream()
                .map(this::updateDeviceState)
                .collect(Collectors.toList());
        for (var future : futures)
            future.get();
        deviceRepository.saveAll(devices);
    }

    /**
     * Update the online state of the single device. Device state is set to online if it is reachable on the network.
     * @param device Device to be updated.
     * @return Returns completable future which is completed when device state is updated.
     */
    private CompletableFuture updateDeviceState(Device device) {
        return deviceClient
                .getState(device.getIp())
                .thenAccept(i -> device.setStatus(i != null ? DeviceStatus.ONLINE : DeviceStatus.OFFLINE))
                .exceptionally(e -> {
                    device.setStatus(DeviceStatus.OFFLINE);
                    return null;
                });
    }

    /**
     * Delete the device.
     * @param deviceId ID of the device to be deleted.
     */
    public void delete(String deviceId) {
        deviceRepository.deleteById(deviceId);
    }
}
