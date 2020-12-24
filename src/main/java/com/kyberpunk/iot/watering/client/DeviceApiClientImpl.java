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

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * This class implements request for controlling ESP32 relay switch. It is capable of switching the wireless relay
 * with connected water pump. It uses HTTP interface with JSON payload.
 * @see <a href="https://github.com/kyberpunk/esp-relay-switch">https://github.com/kyberpunk/esp-relay-switch</a>
 */
@Component
public class DeviceApiClientImpl implements  DeviceApiClient {
    @Async
    public CompletableFuture<DeviceInfo> getState(String ip) {
        var template = createRestTemplate();
        var result = template.getForObject(getRequestUrl(ip), DeviceInfo.class);
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<DeviceInfo> executeSwitchCommand(String ip, boolean state, int timeout) {
        var template = createRestTemplate();
        var command = new SwitchCommand(state, timeout);
        DeviceInfo info = template.postForObject(getRequestUrl(ip), command, DeviceInfo.class);
        return CompletableFuture.completedFuture(info);
    }

    public RestTemplate createRestTemplate() {
        var builder = new RestTemplateBuilder();
        return builder.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
    }

    private String getRequestUrl(String ip) {
        return "http://" + ip + "/api/state";
    }
}
