package com.alenut.planningservice.config;

import com.alenut.planningservice.common.exception.ShiftAlreadyExistsException;
import com.alenut.planningservice.common.exception.ShiftNotFoundException;
import com.alenut.planningservice.common.exception.WorkerAlreadyExistsException;
import com.alenut.planningservice.common.exception.WorkerNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

  private static final String ERROR = "Error: ";

  @ExceptionHandler({WorkerNotFoundException.class, ShiftNotFoundException.class})
  public ResponseEntity<Object> handleWorkerNotFoundException(RuntimeException exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ERROR + exception.getMessage());
  }

  @ExceptionHandler({WorkerAlreadyExistsException.class, ShiftAlreadyExistsException.class})
  public ResponseEntity<Object> handleWorkerAlreadyExistsException(RuntimeException exception) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ERROR + exception.getMessage());
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ERROR + exception.getMessage());
  }

  @ExceptionHandler({MissingServletRequestParameterException.class})
  public ResponseEntity<Object> handleMissingParameterException(MissingServletRequestParameterException exception) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ERROR + exception.getBody().getDetail());
  }


  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors()
        .forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Errors: " + errors);
  }
}
