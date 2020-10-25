package com.kyberpunk.iot.watering.controller;

import com.kyberpunk.iot.watering.dto.ScheduleDto;
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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

@Controller
public class ScheduleController {
    private final DevicesService devicesService;
    private final SchedulesService schedulesService;

    public ScheduleController(DevicesService devicesService, SchedulesService schedulesService) {
        this.devicesService = devicesService;
        this.schedulesService = schedulesService;
    }

    @GetMapping("schedules")
    public String getSchedulesView(Model model) {
        var schedules = schedulesService.findAll();
        model.addAttribute("schedules", schedules);
        return "schedules";
    }

    @GetMapping("schedules/add")
    public String addScheduleView(Model model) {
        model.addAttribute("devices", devicesService.findAll());
        var schedule = new ScheduleDto();
        schedule.setStartAt(LocalDateTime.now());
        model.addAttribute("schedule", schedule);
        return "add_edit_schedule";
    }

    @GetMapping("schedules/edit/{id}")
    public String addScheduleView(@PathVariable Long id, Model model) {
        model.addAttribute("devices", devicesService.findAll());
        ScheduleDto schedule = schedulesService.findById(id);
        model.addAttribute("schedule", schedule);
        return "add_edit_schedule";
    }

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

    @GetMapping("schedules/enable/{id}")
    public String enableSchedule(@PathVariable Long id) {
        schedulesService.enable(id);
        return "redirect:/schedules";
    }

    @GetMapping("schedules/disable/{id}")
    public String disableSchedule(@PathVariable Long id) {
        schedulesService.disable(id);
        return "redirect:/schedules";
    }

    @GetMapping("schedules/delete/{id}")
    public String deleteSchedule(@PathVariable Long id) {
        schedulesService.delete(id);
        return "redirect:/schedules";
    }
}
