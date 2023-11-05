package com.alenut.planningservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO used for creating a worker resource")
public class WorkerBaseDto {

  @NotEmpty
  @Size(max = 100)
  @Schema(description = "E-mail of the worker", example = "workerjoe@gmail.com")
  private String email;

  @NotEmpty
  @Size(max = 100)
  @Schema(description = "Name of the worker", example = "Joe Worker")
  private String name;

  @Size(max = 15)
  @Schema(description = "Phone of the worker", example = "+49 123 456 789")
  private String phone;
}
