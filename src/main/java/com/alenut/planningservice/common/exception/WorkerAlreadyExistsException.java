package com.alenut.planningservice.common.exception;

public class WorkerAlreadyExistsException extends RuntimeException {

  public WorkerAlreadyExistsException(String message) {
    super(message);
  }
}
