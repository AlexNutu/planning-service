package com.alenut.planningservice.dto;

import com.alenut.planningservice.model.enums.ShiftTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used for updating an existing shift resource")
public class ShiftUpdateDto {

  @NotNull
  @Schema(description = "Type of the shift e.g. morning, afternoon, night", implementation = ShiftTypeEnum.class, example = "MORNING")
  private ShiftTypeEnum type;

  @NotNull
  @Schema(description = "Day of the shift", example = "2024-01-01")
  private LocalDate workDay;
}
