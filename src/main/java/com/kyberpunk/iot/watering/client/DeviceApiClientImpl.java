package com.kyberpunk.iot.watering.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

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
