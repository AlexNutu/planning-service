package com.alenut.planningservice.mapper;

import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftDto;
import com.alenut.planningservice.model.entity.Shift;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, builder = @Builder(disableBuilder = true))
public interface ShiftMapper {

  ShiftMapper INSTANCE = Mappers.getMapper(ShiftMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "worker", ignore = true)
  Shift toEntity(ShiftBaseDto dto);

  @Mapping(target = "workerId", source = "worker.id")
  ShiftDto toDto(Shift entity);
}
