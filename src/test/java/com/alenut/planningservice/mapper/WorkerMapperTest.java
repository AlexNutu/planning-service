package com.alenut.planningservice.mapper;

import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorker;
import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorkerBaseDto;
import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorkerUpdateDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerDto;
import com.alenut.planningservice.dto.WorkerUpdateDto;
import com.alenut.planningservice.model.entity.Worker;
import org.junit.jupiter.api.Test;

class WorkerMapperTest {

  @Test
  void toEntity_success() {
    // given
    WorkerBaseDto dto = createWorkerBaseDto();

    // when
    Worker entity = WorkerMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertNull(entity.getShifts());
    assertEquals(dto.getEmail(), entity.getEmail());
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getPhone(), entity.getPhone());
  }

  @Test
  void toDto_success() {
    // given
    Worker entity = createWorker();

    // when
    WorkerDto dto = WorkerMapper.INSTANCE.toDto(entity);

    // then
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getEmail(), dto.getEmail());
    assertEquals(entity.getName(), dto.getName());
    assertEquals(entity.getPhone(), dto.getPhone());
  }

  @Test
  void updateEntityFromDto_success() {
    // given
    Worker entity = new Worker();
    WorkerUpdateDto updateDto = createWorkerUpdateDto();

    // when
    WorkerMapper.INSTANCE.updateEntityFromDto(updateDto, entity);

    // then
    assertNull(entity.getId());
    assertNull(entity.getShifts());
    assertEquals(updateDto.getEmail(), entity.getEmail());
    assertEquals(updateDto.getName(), entity.getName());
    assertEquals(updateDto.getPhone(), entity.getPhone());
  }
}