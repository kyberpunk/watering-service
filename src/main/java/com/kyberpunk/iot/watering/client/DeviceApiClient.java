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

package com.kyberpunk.iot.watering.client;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * This interface contains methods for remote control of wireless watering device. Currently it is strongly coupled
 * with ESP32 relay switch implementation.
 * @see <a href="https://github.com/kyberpunk/esp-relay-switch">https://github.com/kyberpunk/esp-relay-switch</a>
 */
public interface DeviceApiClient {
    /**
     * Get current status of the device.
     * @param ip Target device IP address.
     */
    CompletableFuture<DeviceInfo> getState(String ip);

    /**
     * Switch on the watering on the device. Watering is stopped after timeout. When the timeout is set to 0 then the
     * watering will be running until it is stopped explicitly by calling this request.
     * @param ip Target device IP address.
     * @param state State of the switch to be set. When true then it is switched on.
     * @param timeout Timeout for watering in milliseconds.
     * @return Returns the new device status after command was executed.
     */
    CompletableFuture<DeviceInfo> executeSwitchCommand(String ip, boolean state, int timeout);
    public RestTemplate createRestTemplate();
}
