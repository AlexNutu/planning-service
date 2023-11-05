package com.alenut.planningservice.service.impl;

import static java.lang.String.format;

import com.alenut.planningservice.common.exception.ShiftAlreadyExistsException;
import com.alenut.planningservice.common.exception.ShiftNotFoundException;
import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.mapper.ShiftMapper;
import com.alenut.planningservice.model.entity.Shift;
import com.alenut.planningservice.model.entity.Worker;
import com.alenut.planningservice.repository.ShiftRepository;
import com.alenut.planningservice.service.ShiftService;
import com.alenut.planningservice.service.WorkerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ShiftServiceImpl implements ShiftService {

  private static final String SHIFT_ALREADY_EXISTS_MSG_TEMPLATE = "Shift already exists for worker: [%s], at the date: [%s], with schedule: [%s]";
  private static final String SHIFT_NOT_FOUND_MSG_TEMPLATE = "Shift with id: [%s] was not found";

  private final WorkerService workerService;
  private final ShiftRepository repository;

  @Autowired
  public ShiftServiceImpl(WorkerService workerService, ShiftRepository repository) {
    this.workerService = workerService;
    this.repository = repository;
  }


  @Override
  @Transactional
  public Shift create(@Valid ShiftBaseDto baseDto) {
    Worker worker = workerService.getById(baseDto.getWorkerId());

    checkIfShiftAlreadyExists(worker.getId(), baseDto.getWorkDay());

    Shift shift = ShiftMapper.INSTANCE.toEntity(baseDto);
    shift.setWorker(worker);
    return repository.save(shift);
  }

  @Override
  @Transactional(readOnly = true)
  public Shift getById(@NotNull Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new ShiftNotFoundException(format(SHIFT_NOT_FOUND_MSG_TEMPLATE, id)));
  }

  private void checkIfShiftAlreadyExists(Long workerId, LocalDate workDay) {
    repository.findByWorkerIdAndWorkDay(workerId, workDay).map(Shift::getType)
        .ifPresent(type -> {
          throw new ShiftAlreadyExistsException(
              format(SHIFT_ALREADY_EXISTS_MSG_TEMPLATE, workerId, workDay,
                  type.getStartHour() + "-" + type.getEndHour()));
        });
  }
}
