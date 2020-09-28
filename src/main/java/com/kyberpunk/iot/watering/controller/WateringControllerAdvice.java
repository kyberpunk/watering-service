package com.kyberpunk.iot.watering.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = WateringController.class)
public class WateringControllerAdvice {
    @ModelAttribute("selectedMenuItem")
    public String selectedMenuItem() {
        return "watering";
    }
}
