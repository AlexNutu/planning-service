package com.alenut.planningservice.model.enums;

public enum ShiftTypeEnum {

  MORNING(0, 8),
  AFTERNOON(8, 16),
  NIGHT(16, 24);

  private final Integer startHour;
  private final Integer endHour;

  ShiftTypeEnum(Integer startHour, Integer endHour) {
    this.startHour = startHour;
    this.endHour = endHour;
  }

  public Integer getStartHour() {
    return startHour;
  }

  public Integer getEndHour() {
    return endHour;
  }
}
