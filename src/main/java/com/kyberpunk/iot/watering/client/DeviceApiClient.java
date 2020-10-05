package com.kyberpunk.iot.watering.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DeviceApiClient {
    public DeviceInfo getState(String ip) {
        var template = new RestTemplate();
        return template.getForObject(getRequestUrl(ip), DeviceInfo.class);
    }

    public void executeSwitchCommand(String ip, boolean state, int timeout) {
        var template = new RestTemplate();
        var command = new SwitchCommand(state, timeout);
        template.postForLocation(getRequestUrl(ip), command);
    }

    private String getRequestUrl(String ip) {
        return "http://" + ip + "/api/state";
    }
}
