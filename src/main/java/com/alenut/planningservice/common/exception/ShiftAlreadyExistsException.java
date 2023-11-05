package com.alenut.planningservice.common.exception;

public class ShiftAlreadyExistsException extends RuntimeException {

  public ShiftAlreadyExistsException(String message) {
    super(message);
  }
}
