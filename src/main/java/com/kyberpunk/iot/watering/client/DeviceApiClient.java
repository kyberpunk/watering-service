package com.kyberpunk.iot.watering.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class DeviceApiClient {
    @Async
    public CompletableFuture<DeviceInfo> getState(String ip) {
        var template = new RestTemplate();
        var result = template.getForObject(getRequestUrl(ip), DeviceInfo.class);
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<Boolean> executeSwitchCommand(String ip, boolean state, int timeout) {
        var template = new RestTemplate();
        var command = new SwitchCommand(state, timeout);
        template.postForLocation(getRequestUrl(ip), command);
        return CompletableFuture.completedFuture(true);
    }

    public RestTemplate createRestTemplate()
    {
        var builder = new RestTemplateBuilder();
        return builder.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
    }

    private String getRequestUrl(String ip) {
        return "http://" + ip + "/api/state";
    }
}
