/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, Vit Holasek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

/**
 * Controller for managing devices used for watering. These devices are capable of plants watering.
 * There is devices menu item selected when all views of this controller are shown. See
 * {@link ScheduleControllerAdvice}.
 */
@Controller
public class DeviceController {
    private final DevicesService devicesService;
    private final MessageSource messageSource;

    /**
     * Parametric constructor for dependency injection.
     * @param devicesService {@link DevicesService} instance.
     * @param messageSource {@link MessageSource} instance.
     */
    public DeviceController(DevicesService devicesService, MessageSource messageSource) {
        this.devicesService = devicesService;
        this.messageSource = messageSource;
    }

    /**
     * Get view with all available devices.
     * @param model Model of the request.
     */
    @GetMapping("devices")
    public String getDevicesView(Model model) {
        var devices = devicesService.findAll();
        model.addAttribute("devices", devices);
        return "devices";
    }

    /**
     * Get view for adding new watering device.
     * @param model Model of the request.
     */
    @GetMapping("devices/add")
    public String addDeviceView(Model model) {
        model.addAttribute("device", new NewDeviceDto());
        return "add_device";
    }

    /**
     * Add new device for watering. This device can be selected while creating new schedule or one-time watering.
     * @param device New device to be added.
     * @param bindingResult Binding result of the request. It is used for checking validation errors.
     * @param model Model of the request.
     * @return Redirects to devices view or back to add device view when validation error occurs.
     */
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

    /**
     * Delete existing watering device. If it is attached to any schedule then this schedule is also removed.
     * @param deviceId Device ID.
     * @return Redirects to devices view.
     */
    @GetMapping("devices/delete/{deviceId}")
    public String deleteDevice(@PathVariable String deviceId) {
        devicesService.delete(deviceId);
        return "redirect:/devices";
    }
}
