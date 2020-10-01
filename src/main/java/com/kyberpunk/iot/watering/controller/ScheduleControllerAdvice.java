package com.kyberpunk.iot.watering.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = ScheduleController.class)
public class ScheduleControllerAdvice {
    @ModelAttribute("selectedMenuItem")
    public String selectedMenuItem() {
        return "schedules";
    }
}
