package com.alenut.planningservice.dto;

import com.alenut.planningservice.model.enums.ShiftTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used for creating a shift resource")
public class ShiftBaseDto {

  @NotNull
  @Schema(description = "Id of the worker that is working the shift")
  private Long workerId;

  @NotNull
  @Schema(description = "Type of the shift e.g. morning, afternoon, night")
  private ShiftTypeEnum type;

  @NotNull
  @Schema(description = "Day of the shift")
  private LocalDate workDay;
}
