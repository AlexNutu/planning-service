package com.alenut.planningservice.service;

import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerUpdateDto;
import com.alenut.planningservice.model.entity.Worker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * Worker service. Used for Worker CRUD operations.
 */
@Validated
public interface WorkerService {

  /**
   * Create a new Worker.
   *
   * @param baseDto the input DTO
   * @return the created Worker
   */
  Worker create(@Valid WorkerBaseDto baseDto);


  /**
   * Get a Worker.
   *
   * @param id id of the worker
   * @return the found Worker
   */
  Worker getById(@NotNull Long id);

  /**
   * Update a Worker.
   *
   * @param id        id of the worker
   * @param updateDto the input DTO
   * @return the updated Worker
   */
  Worker update(@NotNull Long id, @Valid WorkerUpdateDto updateDto);

  /**
   * Delete a Worker.
   *
   * @param id id of the worker
   */
  void delete(@NotNull Long id);
}