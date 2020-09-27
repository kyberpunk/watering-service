package com.kyberpunk.iot.watering.controller;

import com.kyberpunk.iot.watering.service.DevicesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeviceController {
    private final DevicesService devicesService;

    public DeviceController(DevicesService devicesService) {
        this.devicesService = devicesService;
    }

    @GetMapping(value={"/", "devices"})
    public String getDevicesView(Model model) {
        var devices = devicesService.findAll();
        model.addAttribute("devices", devices);
        return "devices";
    }

    @GetMapping("devices/add")
    public String addDeviceView(Model model) {
        return "add_device";
    }
}
