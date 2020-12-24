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

/**
 * Controller for managing watering resource. It is used for performing one-time watering actions.
 * There is watering menu item selected when all views of this controller are shown. See
 * {@link WateringControllerAdvice}.
 */
@Controller
public class WateringController {
    private final DevicesService devicesService;
    private final WateringService wateringService;

    /**
     * Parametric constructor for dependency injection.
     * @param devicesService {@link DevicesService} instance.
     * @param wateringService {@link WateringService} instance.
     */
    public WateringController(DevicesService devicesService, WateringService wateringService) {
        this.devicesService = devicesService;
        this.wateringService = wateringService;
    }

    /**
     * Redirect root URL path to one-time watering view.
     */
    @GetMapping("/")
    public String defaultView(Model model) {
        return "redirect:/watering";
    }

    /**
     * Get one-time watering view.
     */
    @GetMapping("watering")
    public String getWateringView(Model model) {
        model.addAttribute("devices", devicesService.findAll());
        model.addAttribute("watering", new WateringDto());
        return "watering";
    }

    /**
     * Perform one-time watering action immediately.
     * @param watering DTO with watering action parameters.
     * @param bindingResult Binding result of the request. It is used for checking validation errors.
     * @param model Model of the request.
     * @return Returns one-time watering view with confirmation/error message.
     */
    @PostMapping("watering")
    public String postWatering(@Validated @ModelAttribute("watering") WateringDto watering,
                               BindingResult bindingResult, Model model) {
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
