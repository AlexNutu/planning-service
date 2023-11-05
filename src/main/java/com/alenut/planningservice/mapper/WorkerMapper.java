package com.alenut.planningservice.mapper;

import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerDto;
import com.alenut.planningservice.model.entity.Worker;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, builder = @Builder(disableBuilder = true))
public interface WorkerMapper {

  WorkerMapper INSTANCE = Mappers.getMapper(WorkerMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "shifts", ignore = true)
  Worker toEntity(WorkerBaseDto dto);

  WorkerDto toDto(Worker entity);
}
