package com.kyberpunk.iot.watering.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WateringController {
    @GetMapping("/")
    public String defaultView(Model model) {
        return "redirect:/watering";
    }

    @GetMapping("watering")
    public String getSchedulesView(Model model) {
        return "watering";
    }
}
