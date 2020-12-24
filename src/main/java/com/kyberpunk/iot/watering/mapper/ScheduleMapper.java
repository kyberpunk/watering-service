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

package com.kyberpunk.iot.watering.mapper;

import com.kyberpunk.iot.watering.dto.ScheduleDto;
import com.kyberpunk.iot.watering.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * This interface defines mapping methods between schedule DTOs and JPA entities. DTOs are used for retrieving data in
 * MVC controller. It uses MapStruct for generating the mapper implementation.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {
    /**
     * Map list of schedule entities to DTOs.
     * @param schedules List of schedule entities.
     * @return Returns list of schedule DTOs.
     */
    List<ScheduleDto> entityToDto(List<Schedule> schedules);

    /**
     * Map single schedule entity to DTO.
     * @param schedule Schedule entity.
     * @return Returns schedule DTO.
     */
    ScheduleDto entityToDto(Schedule schedule);

    /**
     * Map list of schedule DTOs to JPA entities.
     * @param dtos List of schedule DTOs.
     * @return Returns list of schedule entities.
     */
    List<Schedule> dtoToEntity(List<ScheduleDto> dtos);

    /**
     * Map single schedule DTO to JPA entity.
     * @param dto Schedule DTO.
     * @return Returns schedule entity.
     */
    Schedule dtoToEntity(ScheduleDto dto);
}
