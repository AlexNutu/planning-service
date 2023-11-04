package com.alenut.planningservice.common.exception;

public class WorkerNotFoundException extends RuntimeException {

  public WorkerNotFoundException(String message) {
    super(message);
  }
}
