package com.alenut.planningservice.datagenerator;

import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftUpdateDto;
import com.alenut.planningservice.model.entity.Shift;
import com.alenut.planningservice.model.entity.Worker;
import com.alenut.planningservice.model.enums.ShiftTypeEnum;
import java.time.LocalDate;

public class ShiftDataGenerator {

  private ShiftDataGenerator() {
  }

  public static ShiftBaseDto createShiftBaseDto(Long workerId) {
    ShiftBaseDto shiftBaseDto = new ShiftBaseDto();
    shiftBaseDto.setWorkerId(workerId);
    shiftBaseDto.setType(ShiftTypeEnum.MORNING);
    shiftBaseDto.setWorkDay(LocalDate.parse("2024-01-01"));
    return shiftBaseDto;
  }

  public static ShiftUpdateDto createShiftUpdateDto() {
    ShiftUpdateDto updateDto = new ShiftUpdateDto();
    updateDto.setType(ShiftTypeEnum.NIGHT);
    updateDto.setWorkDay(LocalDate.parse("2025-02-05"));
    return updateDto;
  }

  public static Shift createShift(Long workerId) {
    Worker worker = new Worker();
    worker.setId(workerId);
    Shift shift = new Shift();
    shift.setId(1L);
    shift.setWorker(worker);
    shift.setType(ShiftTypeEnum.MORNING);
    shift.setWorkDay(LocalDate.parse("2024-01-01"));
    return shift;
  }

}
