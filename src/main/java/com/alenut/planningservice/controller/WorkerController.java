package com.alenut.planningservice.controller;

import static com.alenut.planningservice.common.controller.Constants.PLANNING;
import static com.alenut.planningservice.controller.WorkerController.PATH_WORKERS;

import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerDto;
import com.alenut.planningservice.mapper.WorkerMapper;
import com.alenut.planningservice.model.entity.Worker;
import com.alenut.planningservice.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PATH_WORKERS)
@Validated
@Tag(name = "Workers")
public class WorkerController {

  static final String PATH_WORKERS = PLANNING + "/workers";

  private final WorkerService workerService;

  @Autowired
  public WorkerController(WorkerService workerService) {
    this.workerService = workerService;
  }

  @PostMapping
  @Operation(summary = "Create a new worker")
  public ResponseEntity<WorkerDto> create(@Valid @RequestBody WorkerBaseDto baseDto) {
    Worker worker = workerService.create(baseDto);
    WorkerDto workerDto = WorkerMapper.INSTANCE.toDto(worker);
    return ResponseEntity.ok(workerDto);
  }

  @GetMapping(value = "/{id}")
  @Operation(summary = "Get worker by id")
  public ResponseEntity<WorkerDto> findById(@PathVariable Long id) {
    Worker worker = workerService.getById(id);
    WorkerDto workerDto = WorkerMapper.INSTANCE.toDto(worker);
    return ResponseEntity.ok(workerDto);
  }
}
