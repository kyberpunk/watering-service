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

import com.kyberpunk.iot.watering.dto.DeviceDto;
import com.kyberpunk.iot.watering.dto.NewDeviceDto;
import com.kyberpunk.iot.watering.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * This interface defines mapping methods between device DTOs and JPA entities. DTOs are used for retrieving data in
 * MVC controller. It uses MapStruct for generating the mapper implementation.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper {
    /**
     * Map list of device entities to DTOs.
     * @param devices List of device entities.
     * @return Returns list of device DTOs.
     */
    List<DeviceDto> entityToDto(List<Device> devices);

    /**
     * Map single device entity to DTO.
     * @param device Device entity.
     * @return Returns device DTO.
     */
    DeviceDto entityToDto(Device device);

    /**
     * Map list of device DTOs to JPA entities.
     * @param dtos List of device DTOs.
     * @return Returns list of device entities.
     */
    List<Device> dtoToEntity(List<DeviceDto> dtos);

    /**
     * Map single device DTO to JPA entity.
     * @param dto Device DTO.
     * @return Returns device entity.
     */
    Device dtoToEntity(DeviceDto dto);

    /**
     * Map new device DTO to JPA entity.
     * @param dto New device DTO.
     * @return Returns device entity.
     */
    Device dtoToEntity(NewDeviceDto dto);
}
