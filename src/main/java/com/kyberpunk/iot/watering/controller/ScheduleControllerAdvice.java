package com.kyberpunk.iot.watering.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@ControllerAdvice(assignableTypes = ScheduleController.class)
public class ScheduleControllerAdvice {
    @ModelAttribute("selectedMenuItem")
    public String selectedMenuItem() {
        return "schedules";
    }
}
