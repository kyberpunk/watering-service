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

package com.kyberpunk.iot.watering.dto;

import com.kyberpunk.iot.watering.model.TimeUnit;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * This class is used as DTO for watering schedules resource.
 */
@Data
public class ScheduleDto {
    /**
     * Schedule ID.
     */
    private long id;
    /**
     * Determines if schedule is enabled or not. If schedule is not active then it is not executed.
     */
    private boolean active;
    /**
     * Description of the schedule filled by the user.
     */
    @NotEmpty(message = "{validation.description_empty}")
    private String description;
    /**
     * ID of the device which will be used for watering performed by this schedule.
     */
    @NotEmpty(message = "{validation.device_empty}")
    private String deviceId;
    /**
     * Name of the device which will be used for watering performed by this schedule. I contains description of device
     * or its device ID in case that description is not filled. Is is used just for displaying the human readable name.
     */
    private String deviceDisplayValue;
    /**
     * Length of the watering interval after which is watering executed.
     */
    @Min(value = 1, message = "{validation.interval_min}")
    private int interval;
    /**
     * Time unit of the watering interval.
     */
    private TimeUnit unit;
    /**
     * Duration of the watering in seconds. It defines how long is the water pump running when watering is executed.
     */
    @Max(value = 30, message = "{validation.duration_max}")
    @Min(value = 1, message = "{validation.duration_min}")
    private int duration;
    /**
     * Time when the first watering will be executed.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startAt;
    /**
     * Time when the last watering was executed.
     */
    private LocalDateTime lastWatered;
}
