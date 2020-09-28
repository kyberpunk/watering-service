package com.kyberpunk.iot.watering.mapper;

import com.kyberpunk.iot.watering.dto.DeviceDto;
import com.kyberpunk.iot.watering.dto.NewDeviceDto;
import com.kyberpunk.iot.watering.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * This interface defines mapping methods for controller DTO.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper {
    List<DeviceDto> entityToDto(List<Device> devices);
    DeviceDto entityToDto(Device device);
    List<Device> dtoToEntity(List<DeviceDto> dto);
    Device dtoToEntity(DeviceDto dto);
    Device dtoToEntity(NewDeviceDto dto);
}
