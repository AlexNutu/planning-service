package com.alenut.planningservice.service;

import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftUpdateDto;
import com.alenut.planningservice.model.entity.Shift;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.validation.annotation.Validated;

/**
 * Shift service. Used for Shift CRUD operations.
 */
@Validated
public interface ShiftService {

  /**
   * Create a new Shift.
   *
   * @param baseDto the input DTO
   * @return the created Shift
   */
  Shift create(@Valid ShiftBaseDto baseDto);


  /**
   * Get a Shift.
   *
   * @param id id of the shift
   * @return the found Shift
   */
  Shift getById(@NotNull Long id);

  /**
   * Get a list of Shifts.
   *
   * @param workerId id of the worker
   * @return the found Shifts
   */
  List<Shift> findByWorkerId(@NotNull Long workerId);

  /**
   * Update a Shift.
   *
   * @param id        id of the shift
   * @param updateDto the input DTO
   * @return the updated Shift
   */
  Shift update(@NotNull Long id, @Valid ShiftUpdateDto updateDto);

  /**
   * Delete a Shift.
   *
   * @param id id of the shift
   */
  void delete(@NotNull Long id);
}
