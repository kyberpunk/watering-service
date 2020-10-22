package com.kyberpunk.iot.watering.controller;

import com.kyberpunk.iot.watering.dto.NewDeviceDto;
import com.kyberpunk.iot.watering.service.DevicesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class DeviceController {
    private final DevicesService devicesService;

    public DeviceController(DevicesService devicesService) {
        this.devicesService = devicesService;
    }

    @GetMapping("devices")
    public String getDevicesView(Model model) {
        var devices = devicesService.findAll();
        model.addAttribute("devices", devices);
        return "devices";
    }

    @GetMapping("devices/add")
    public String addDeviceView(Model model) {
        model.addAttribute("device", new NewDeviceDto());
        return "add_device";
    }

    @PostMapping("devices")
    public String addDevice(@Validated @ModelAttribute("device") NewDeviceDto device, BindingResult bindingResult, Model model) {
        if (device != null && device.getDeviceId() != null
                && devicesService.findById(device.getDeviceId()).isPresent()) {
            bindingResult.rejectValue("deviceId", "Id Not Unique", "Device ID already exists.");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("device", device);
            return "add_device";
        }
        devicesService.create(device);
        return "redirect:/devices";
    }

    @GetMapping("devices/delete/{deviceId}")
    public String deleteDevice(@PathVariable String deviceId) {
        devicesService.delete(deviceId);
        return "redirect:/devices";
    }
}
