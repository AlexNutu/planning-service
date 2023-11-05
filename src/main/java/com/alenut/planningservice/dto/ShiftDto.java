package com.alenut.planningservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShiftDto extends ShiftBaseDto {

  @Schema(description = "The db ID of the shift resource")
  private Long id;
}
