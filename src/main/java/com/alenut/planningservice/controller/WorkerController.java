package com.alenut.planningservice.controller;

import static com.alenut.planningservice.common.controller.Constants.PLANNING;
import static com.alenut.planningservice.controller.WorkerController.PATH_WORKERS;

import com.alenut.planningservice.common.exception.WorkerAlreadyExistsException;
import com.alenut.planningservice.common.exception.WorkerNotFoundException;
import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

  @GetMapping("/{id}")
  public ResponseEntity<WorkerDto> findById(@PathVariable Long id) {
    // TODO: 04.11.2023

    if (id == -1) {
      throw new WorkerNotFoundException("Worker not found");
    }

    return ResponseEntity.ok(new WorkerDto());
  }

  @PostMapping("/")
  public ResponseEntity<WorkerDto> create(@Valid @RequestBody WorkerBaseDto baseDto) {
    // TODO: 04.11.2023

    if (baseDto.getName().length() > 2) {
      throw new WorkerAlreadyExistsException("Worker already exists");
    }

    return ResponseEntity.ok(new WorkerDto());
  }
}
