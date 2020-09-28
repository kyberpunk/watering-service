package com.kyberpunk.iot.watering.controller;

import com.kyberpunk.iot.watering.dto.NewDeviceDto;
import com.kyberpunk.iot.watering.service.DevicesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WateringController {
    @GetMapping("/")
    public String defaultView(Model model) {
        return "redirect:/watering";
    }

    @GetMapping("watering")
    public String getWateringView(Model model) {
        return "watering";
    }
}
