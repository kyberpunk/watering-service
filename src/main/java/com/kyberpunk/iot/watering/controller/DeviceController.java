package com.kyberpunk.iot.watering.controller;

import com.kyberpunk.iot.watering.dto.NewDeviceDto;
import com.kyberpunk.iot.watering.service.DevicesService;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class DeviceController {
    private final DevicesService devicesService;
    private final MessageSource messageSource;

    public DeviceController(DevicesService devicesService, MessageSource messageSource) {
        this.devicesService = devicesService;
        this.messageSource = messageSource;
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
            String deviceIdExists = messageSource.getMessage("validation.device_id_exists", null, LocaleContextHolder.getLocale());
            bindingResult.rejectValue("deviceId", "Id Not Unique", deviceIdExists);
        }
        var inetValidator = InetAddressValidator.getInstance();
        if (!inetValidator.isValid(device.getIp())) {
            String ipNotValid = messageSource.getMessage("validation.ip_not_valid", null, LocaleContextHolder.getLocale());
            bindingResult.rejectValue("ip", "IP Not Valid", ipNotValid);
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
