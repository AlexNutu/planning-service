package com.alenut.planningservice.mapper;

import static com.alenut.planningservice.datagenerator.ShiftDataGenerator.createShift;
import static com.alenut.planningservice.datagenerator.ShiftDataGenerator.createShiftBaseDto;
import static com.alenut.planningservice.datagenerator.ShiftDataGenerator.createShiftUpdateDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftDto;
import com.alenut.planningservice.dto.ShiftUpdateDto;
import com.alenut.planningservice.model.entity.Shift;
import org.junit.jupiter.api.Test;

class ShiftMapperTest {

  @Test
  void toEntity_success() {
    // given
    ShiftBaseDto dto = createShiftBaseDto(1L);

    // when
    Shift entity = ShiftMapper.INSTANCE.toEntity(dto);

    // then
    assertNull(entity.getId());
    assertNull(entity.getWorker());
    assertEquals(dto.getType(), entity.getType());
    assertEquals(dto.getWorkDay(), entity.getWorkDay());
  }

  @Test
  void toDto_success() {
    // given
    Shift entity = createShift(1L);

    // when
    ShiftDto dto = ShiftMapper.INSTANCE.toDto(entity);

    // then
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getWorker().getId(), dto.getWorkerId());
    assertEquals(entity.getType(), dto.getType());
    assertEquals(entity.getWorkDay(), dto.getWorkDay());
  }

  @Test
  void updateEntityFromDto_success() {
    // given
    Shift entity = new Shift();
    ShiftUpdateDto updateDto = createShiftUpdateDto();

    // when
    ShiftMapper.INSTANCE.updateEntityFromDto(updateDto, entity);

    // then
    assertNull(entity.getId());
    assertNull(entity.getWorker());
    assertEquals(updateDto.getType(), entity.getType());
    assertEquals(updateDto.getWorkDay(), entity.getWorkDay());
  }
}