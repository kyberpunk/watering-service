package com.kyberpunk.iot.watering.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controller advice for devices controller.
 */
@ControllerAdvice(assignableTypes = DeviceController.class)
public class DeviceControllerAdvice {
    /**
     * Explicitly set devices menu item to be selected in client view.
     */
    @ModelAttribute("selectedMenuItem")
    public String selectedMenuItem() {
        return "devices";
    }
}
