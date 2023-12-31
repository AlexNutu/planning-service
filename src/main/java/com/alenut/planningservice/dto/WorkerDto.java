package com.alenut.planningservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerDto extends WorkerBaseDto {

  @Schema(description = "The db ID of the worker resource")
  private Long id;
}
