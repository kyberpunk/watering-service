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
import com.kyberpunk.iot.watering.client.DeviceInfo;
import com.kyberpunk.iot.watering.dto.WateringDto;
import com.kyberpunk.iot.watering.repository.DeviceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * This service class implements methods for performing watering on the device.
 */
@Log4j2
@Service
public class WateringService {
    private final DeviceApiClient client;
    private final DeviceRepository deviceRepository;

    /**
     * Parametric constructor for dependency injection.
     * @param client {@link DeviceApiClient} instance.
     * @param deviceRepository {@link DeviceRepository} instance.
     */
    public WateringService(DeviceApiClient client, DeviceRepository deviceRepository) {
        this.client = client;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Perform watering on the device immediately. This method is executed synchronously.
     * {@link java.util.NoSuchElementException} is thrown when target device does not exist.
     * @param watering Watering operation to be performed.
     */
    public void waterNow(WateringDto watering) {
        var device = deviceRepository.findById(watering.getDeviceId()).orElseThrow();
        device.setLastWatered(LocalDateTime.now());
        try {
            DeviceInfo info = client.executeSwitchCommand(device.getIp(), true, watering.getDuration() * 1000).get();
            if (info.isSwitchedOn())
                deviceRepository.save(device);
        } catch (Exception e) {
            log.warn("Execution of watering failed.", e);
            throw new WateringException(this.getClass().getName() + ": nested exception: " + e.getMessage(), e);
        }
    }
}
