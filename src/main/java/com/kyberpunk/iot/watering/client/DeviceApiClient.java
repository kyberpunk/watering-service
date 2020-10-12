package com.kyberpunk.iot.watering.client;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
    public Future<Boolean> executeSwitchCommand(String ip, boolean state, int timeout) {
        var template = new RestTemplate();
        var command = new SwitchCommand(state, timeout);
        template.postForLocation(getRequestUrl(ip), command);
        return new AsyncResult(true);
    }

    private String getRequestUrl(String ip) {
        return "http://" + ip + "/api/state";
    }
}
