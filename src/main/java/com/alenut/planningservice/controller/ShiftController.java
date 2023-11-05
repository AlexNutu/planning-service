package com.alenut.planningservice.controller;

import static com.alenut.planningservice.common.controller.Constants.PLANNING;
import static com.alenut.planningservice.controller.ShiftController.PATH_SHIFTS;

import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftDto;
import com.alenut.planningservice.mapper.ShiftMapper;
import com.alenut.planningservice.model.entity.Shift;
import com.alenut.planningservice.service.ShiftService;
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

  @PostMapping("/")
  public ResponseEntity<ShiftDto> create(@Valid @RequestBody ShiftBaseDto baseDto) {
    Shift shift = shiftService.create(baseDto);
    ShiftDto shiftDto = ShiftMapper.INSTANCE.toDto(shift);
    return ResponseEntity.ok(shiftDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ShiftDto> findById(@PathVariable Long id) {
    Shift shift = shiftService.getById(id);
    ShiftDto shiftDto = ShiftMapper.INSTANCE.toDto(shift);
    return ResponseEntity.ok(shiftDto);
  }
}
