package com.kyberpunk.iot.watering.client;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

public interface DeviceApiClient {
    CompletableFuture<DeviceInfo> getState(String ip);
    CompletableFuture<DeviceInfo> executeSwitchCommand(String ip, boolean state, int timeout);
    public RestTemplate createRestTemplate();
}
