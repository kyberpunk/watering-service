package com.kyberpunk.iot.watering.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SwitchCommand {
    private boolean switchedOn;
    private int timeout;
}
