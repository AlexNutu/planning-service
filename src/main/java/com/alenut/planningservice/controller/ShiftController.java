package com.alenut.planningservice.controller;

import static com.alenut.planningservice.common.controller.Constants.PLANNING;
import static com.alenut.planningservice.controller.ShiftController.PATH_SHIFTS;

import com.alenut.planningservice.common.exception.ShiftAlreadyExistsException;
import com.alenut.planningservice.common.exception.ShiftNotFoundException;
import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftDto;
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
@RequestMapping(PATH_SHIFTS)
@Validated
@Tag(name = "Shifts")
public class ShiftController {

  static final String PATH_SHIFTS = PLANNING + "/shifts";

  @PostMapping("/")
  public ResponseEntity<ShiftDto> create(@Valid @RequestBody ShiftBaseDto baseDto) {
    // TODO: 04.11.2023

    if (baseDto.getWorkerId() == null) {
      throw new ShiftAlreadyExistsException("Shift already exists");
    }

    return ResponseEntity.ok(new ShiftDto());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ShiftDto> findById(@PathVariable Long id) {
    // TODO: 04.11.2023

    if (id == -1) {
      throw new ShiftNotFoundException("Shift not found");
    }

    return ResponseEntity.ok(new ShiftDto());
  }
}
