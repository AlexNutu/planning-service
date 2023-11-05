package com.alenut.planningservice.service;

import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.model.entity.Worker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface WorkerService {

  Worker create(@Valid WorkerBaseDto baseDto);

  Worker getById(@NotNull Long id);
}