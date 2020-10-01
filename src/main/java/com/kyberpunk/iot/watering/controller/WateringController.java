package com.kyberpunk.iot.watering.controller;

import com.kyberpunk.iot.watering.dto.WateringDto;
import com.kyberpunk.iot.watering.service.DevicesService;
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

    public WateringController(DevicesService devicesService) {
        this.devicesService = devicesService;
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
        if (bindingResult.hasErrors()) {
            return "watering";
        } else {
            model.addAttribute("success", true);
        }
        model.addAttribute("devices", devicesService.findAll());
        return "watering";
    }
}
