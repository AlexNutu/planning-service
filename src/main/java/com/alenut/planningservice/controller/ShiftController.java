package com.alenut.planningservice.controller;

import static com.alenut.planningservice.common.controller.Constants.PLANNING;
import static com.alenut.planningservice.controller.ShiftController.PATH_SHIFTS;

import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftDto;
import com.alenut.planningservice.dto.ShiftListDto;
import com.alenut.planningservice.dto.ShiftUpdateDto;
import com.alenut.planningservice.mapper.ShiftMapper;
import com.alenut.planningservice.model.entity.Shift;
import com.alenut.planningservice.service.ShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PATH_SHIFTS)
@Validated
@Tag(name = "Shifts")
public class ShiftController {

  static final String PATH_SHIFTS = PLANNING + "/shifts";

  private final ShiftService shiftService;

  @Autowired
  public ShiftController(ShiftService shiftService) {
    this.shiftService = shiftService;
  }

  @PostMapping
  @Operation(summary = "Create a new shift")
  public ResponseEntity<ShiftDto> create(@Valid @RequestBody ShiftBaseDto baseDto) {
    Shift shift = shiftService.create(baseDto);
    ShiftDto shiftDto = ShiftMapper.INSTANCE.toDto(shift);
    return ResponseEntity.ok(shiftDto);
  }

  @GetMapping(value = "/{id}")
  @Operation(summary = "Get shift by id")
  public ResponseEntity<ShiftDto> findById(@PathVariable Long id) {
    Shift shift = shiftService.getById(id);
    ShiftDto shiftDto = ShiftMapper.INSTANCE.toDto(shift);
    return ResponseEntity.ok(shiftDto);
  }

  @GetMapping
  @Operation(summary = "Get all shifts by worker id")
  public ResponseEntity<ShiftListDto> findByWorkerId(@RequestParam(name = "worker_id") Long workerId) {
    List<ShiftDto> elements = shiftService.findByWorkerId(workerId)
        .stream().map(ShiftMapper.INSTANCE::toDto).toList();

    return ResponseEntity.ok().body(ShiftListDto.of(elements));
  }

  @PutMapping(value = "/{id}")
  @Operation(summary = "Update shift by id")
  public ResponseEntity<ShiftDto> update(@PathVariable Long id, @Valid @RequestBody ShiftUpdateDto updateDto) {

    Shift shift = shiftService.update(id, updateDto);
    ShiftDto shiftDto = ShiftMapper.INSTANCE.toDto(shift);

    return ResponseEntity.ok(shiftDto);
  }

  @DeleteMapping(value = "/{id}")
  @Operation(summary = "Delete shift by id")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    shiftService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
