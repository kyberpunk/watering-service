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

import com.kyberpunk.iot.watering.dto.ScheduleDto;
import com.kyberpunk.iot.watering.model.TimeUnit;
import com.kyberpunk.iot.watering.service.DevicesService;
import com.kyberpunk.iot.watering.service.SchedulesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

/**
 * Controller for managing watering schedules. It is used for performing one-time watering actions.
 * Schedules are used for automatic periodical watering. There is schedules menu item selected when
 * all views of this controller are shown. See {@link ScheduleControllerAdvice}.
 */
@Controller
public class ScheduleController {
    private final DevicesService devicesService;
    private final SchedulesService schedulesService;

    /**
     * Parametric constructor for dependency injection.
     * @param devicesService {@link DevicesService} instance.
     * @param schedulesService {@link SchedulesService} instance.
     */
    public ScheduleController(DevicesService devicesService, SchedulesService schedulesService) {
        this.devicesService = devicesService;
        this.schedulesService = schedulesService;
    }

    /**
     * Get view with all schedules.
     * @param model Model of the request.
     */
    @GetMapping("schedules")
    public String getSchedulesView(Model model) {
        var schedules = schedulesService.findAll();
        model.addAttribute("schedules", schedules);
        return "schedules";
    }

    /**
     * Get view for adding new schedules.
     * @param model Model of the request.
     */
    @GetMapping("schedules/add")
    public String addScheduleView(Model model) {
        model.addAttribute("devices", devicesService.findAll());
        var schedule = new ScheduleDto();
        schedule.setStartAt(LocalDateTime.now());
        schedule.setUnit(TimeUnit.DAYS);
        model.addAttribute("schedule", schedule);
        return "add_edit_schedule";
    }

    /**
     * Get view for editing existing schedules.
     * @param id Schedule ID.
     * @param model Model of the request.
     */
    @GetMapping("schedules/edit/{id}")
    public String addScheduleView(@PathVariable Long id, Model model) {
        model.addAttribute("devices", devicesService.findAll());
        ScheduleDto schedule = schedulesService.findById(id);
        model.addAttribute("schedule", schedule);
        return "add_edit_schedule";
    }

    /**
     * Create new schedule. Schedule became active after created.
     * @param schedule Schedule to be created.
     * @param bindingResult Binding result of the request. It is used for checking validation errors.
     * @param model Model of the request.
     * @return Redirects to all schedules view. If there is a validation error then edit view with appropriate error
     * messages is returned.
     */
    @PostMapping("schedules")
    public String postSchedule(@Validated @ModelAttribute("schedule") ScheduleDto schedule, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("devices", devicesService.findAll());
            return "add_edit_schedule";
        }
        if (schedule.getId() == 0) {
            schedulesService.create(schedule);
        } else {
            schedulesService.update(schedule);
        }
        return "redirect:/schedules";
    }

    /**
     * Enable the existing schedule. Enabled schedule will be processed when scheduled time elapses.
     * @param id Schedule ID.
     * @return Redirects to all schedules view.
     */
    @GetMapping("schedules/enable/{id}")
    public String enableSchedule(@PathVariable Long id) {
        schedulesService.enable(id);
        return "redirect:/schedules";
    }

    /**
     * Disable the existing schedule. Disabled schedule is not processed. When scheduled time elapsed then schedule
     * is executed immediately after it is enabled again.
     * @param id Schedule ID.
     * @return Redirects to all schedules view.
     */
    @GetMapping("schedules/disable/{id}")
    public String disableSchedule(@PathVariable Long id) {
        schedulesService.disable(id);
        return "redirect:/schedules";
    }

    /**
     * Delete the existing schedule.
     * @param id Schedule ID.
     * @return Redirects to all schedules view.
     */
    @GetMapping("schedules/delete/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        schedulesService.delete(id);
        return "redirect:/schedules";
    }
}
