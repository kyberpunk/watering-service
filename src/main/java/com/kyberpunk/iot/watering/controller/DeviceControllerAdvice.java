package com.kyberpunk.iot.watering.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = DeviceController.class)
public class DeviceControllerAdvice {
    @ModelAttribute("selectedMenuItem")
    public String selectedMenuItem() {
        return "devices";
    }
}
