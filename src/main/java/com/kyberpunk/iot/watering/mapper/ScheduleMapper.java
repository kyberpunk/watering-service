package com.kyberpunk.iot.watering.mapper;

import com.kyberpunk.iot.watering.dto.ScheduleDto;
import com.kyberpunk.iot.watering.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * This interface defines mapping methods for controller DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduleMapper {
    List<ScheduleDto> entityToDto(List<Schedule> schedules);
    ScheduleDto entityToDto(Schedule schedule);
    List<Schedule> dtoToEntity(List<ScheduleDto> dto);
    Schedule dtoToEntity(ScheduleDto dto);
}
