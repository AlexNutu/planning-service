package com.alenut.planningservice.service.impl;

import static java.lang.String.format;

import com.alenut.planningservice.common.exception.WorkerAlreadyExistsException;
import com.alenut.planningservice.common.exception.WorkerNotFoundException;
import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerUpdateDto;
import com.alenut.planningservice.mapper.WorkerMapper;
import com.alenut.planningservice.model.entity.Worker;
import com.alenut.planningservice.repository.WorkerRepository;
import com.alenut.planningservice.service.WorkerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class WorkerServiceImpl implements WorkerService {

  public static final String WORKER_ALREADY_EXISTS_MSG_TEMPLATE = "Worker with e-mail: [%s] already exists";
  public static final String WORKER_NOT_FOUND_MSG_TEMPLATE = "Worker with id: [%s] was not found";
  private final WorkerRepository repository;

  @Autowired
  public WorkerServiceImpl(WorkerRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Worker create(@Valid WorkerBaseDto baseDto) {
    checkIfWorkerAlreadyExists(baseDto);

    Worker worker = WorkerMapper.INSTANCE.toEntity(baseDto);
    return repository.save(worker);
  }

  @Override
  @Transactional(readOnly = true)
  public Worker getById(@NotNull Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new WorkerNotFoundException(format(WORKER_NOT_FOUND_MSG_TEMPLATE, id)));
  }

  @Override
  @Transactional
  public Worker update(@NotNull Long id, @Valid WorkerUpdateDto updateDto) {
    Worker worker = getById(id);

    checkIfWorkerAlreadyExists(updateDto);

    WorkerMapper.INSTANCE.updateEntityFromDto(updateDto, worker);
    return repository.save(worker);
  }

  @Override
  @Transactional
  public void delete(@NotNull Long id) {
    Worker worker = getById(id);
    repository.delete(worker);
  }

  private void checkIfWorkerAlreadyExists(WorkerBaseDto baseDto) {
    Worker existingWorker = repository.findByEmail(baseDto.getEmail()).orElse(null);
    if (existingWorker != null) {
      throw new WorkerAlreadyExistsException(format(WORKER_ALREADY_EXISTS_MSG_TEMPLATE, baseDto.getEmail()));
    }
  }
}
