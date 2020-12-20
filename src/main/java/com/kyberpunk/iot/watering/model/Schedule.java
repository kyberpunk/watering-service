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

package com.kyberpunk.iot.watering.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * This class define watering schedule entity which is persisted in the database.
 */
@Data
@Entity
@ToString
public class Schedule {
    /**
     * Schedule ID.
     */
    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    /**
     * Determines if schedule is enabled or not. If it is not enabled it won't be executed when time elapses.
     */
    private boolean active;
    /**
     * Description of the schedule filled by the use.
     */
    private String description;
    /**
     * Device on which is executed watering defined by this schedule.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "device_id")
    private Device device;
    /**
     * Length of the watering interval after which is watering executed.
     */
    @Column(name="intervalValue")
    private int interval;
    /**
     * Time unit of the watering interval.
     */
    private TimeUnit unit;
    /**
     * Duration of the watering. It defines how long is the water pump running when watering is executed.
     */
    private int duration;
    /**
     * Time when the first watering will be executed.
     */
    @Column(nullable = false)
    private LocalDateTime startAt;
    /**
     * Time when the last watering was executed.
     */
    private LocalDateTime lastWatered;
}
