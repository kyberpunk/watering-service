package com.kyberpunk.iot.watering.controller;

import com.kyberpunk.iot.watering.dto.WateringDto;
import com.kyberpunk.iot.watering.service.DevicesService;
import com.kyberpunk.iot.watering.service.WateringException;
import com.kyberpunk.iot.watering.service.WateringService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WateringController {
    private final DevicesService devicesService;
    private final WateringService wateringService;

    public WateringController(DevicesService devicesService, WateringService wateringService) {
        this.devicesService = devicesService;
        this.wateringService = wateringService;
    }

    @GetMapping("/")
    public String defaultView(Model model) {
        return "redirect:/watering";
    }

    @GetMapping("watering")
    public String getWateringView(Model model) {
        model.addAttribute("devices", devicesService.findAll());
        model.addAttribute("watering", new WateringDto());
        return "watering";
    }

    @PostMapping("watering")
    public String postWatering(@Validated @ModelAttribute("watering") WateringDto watering, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                wateringService.waterNow(watering);
                model.addAttribute("success", "");
            } catch (WateringException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        model.addAttribute("devices", devicesService.findAll());
        return "watering";
    }
}
