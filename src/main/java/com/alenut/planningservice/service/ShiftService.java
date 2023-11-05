package com.alenut.planningservice.service;

import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.model.entity.Shift;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ShiftService {

  Shift create(@Valid ShiftBaseDto baseDto);

  Shift getById(@NotNull Long id);
}
