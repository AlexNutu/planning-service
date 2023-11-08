package com.alenut.planningservice.service;

import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorker;
import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorkerBaseDto;
import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorkerUpdateDto;
import static com.alenut.planningservice.service.impl.WorkerServiceImpl.WORKER_ALREADY_EXISTS_MSG_TEMPLATE;
import static com.alenut.planningservice.service.impl.WorkerServiceImpl.WORKER_NOT_FOUND_MSG_TEMPLATE;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.alenut.planningservice.common.exception.WorkerAlreadyExistsException;
import com.alenut.planningservice.common.exception.WorkerNotFoundException;
import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerUpdateDto;
import com.alenut.planningservice.model.entity.Worker;
import com.alenut.planningservice.repository.WorkerRepository;
import com.alenut.planningservice.service.impl.WorkerServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class WorkerServiceTest {

  @InjectMocks
  private WorkerServiceImpl workerService;

  @Mock
  private WorkerRepository workerRepository;

  @BeforeEach
  void init() {
    openMocks(this);
  }

  @Test
  void create_isSuccessful_whenValidDataProvided() {
    // Given
    when(workerRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
    saveWorkerStubbing();
    WorkerBaseDto workerBaseDto = createWorkerBaseDto();

    // When
    Worker worker = workerService.create(workerBaseDto);

    // Then
    assertEquals(1L, worker.getId());
    assertEquals(workerBaseDto.getEmail(), worker.getEmail());
    assertEquals(workerBaseDto.getName(), worker.getName());
    assertEquals(workerBaseDto.getPhone(), worker.getPhone());
  }

  @Test
  void create_shouldThrowWorkerAlreadyExistsException_whenWorkerAlreadyExists() {
    // Given
    WorkerBaseDto workerBaseDto = createWorkerBaseDto();
    WorkerAlreadyExistsException exception = new WorkerAlreadyExistsException(
        format(WORKER_ALREADY_EXISTS_MSG_TEMPLATE, workerBaseDto.getEmail()));
    when(workerRepository.findByEmail(any(String.class))).thenReturn(Optional.of(new Worker()));

    // When
    WorkerAlreadyExistsException ex = assertThrows(WorkerAlreadyExistsException.class,
        () -> workerService.create(workerBaseDto));

    // Then
    assertEquals(exception.getMessage(), ex.getMessage());
  }

  @Test
  void getById_isSuccessful_whenDataIsFound() {
    // Given
    Worker entity = createWorker();
    when(workerRepository.findById(any(Long.class))).thenReturn(Optional.of(entity));

    // When
    Worker worker = workerService.getById(1L);

    // Then
    assertEquals(entity.getId(), worker.getId());
    assertEquals(entity.getEmail(), worker.getEmail());
    assertEquals(entity.getName(), worker.getName());
    assertEquals(entity.getPhone(), worker.getPhone());
  }

  @Test
  void getById_shouldThrowWorkerNotFoundException_whenWorkerIdIsUnknown() {
    // Given
    when(workerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

    // When
    WorkerNotFoundException exc =
        assertThrows(WorkerNotFoundException.class, () -> workerService.getById(1L));

    // Then
    assertThat(exc.getMessage(),
        containsString(format(WORKER_NOT_FOUND_MSG_TEMPLATE, 1L)));
  }

  @Test
  void update_isSuccessful_whenValidDataProvided() {
    // Given
    when(workerRepository.findById(any(Long.class))).thenReturn(Optional.of(createWorker()));
    when(workerRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
    saveWorkerStubbing();
    WorkerUpdateDto workerUpdateDto = createWorkerUpdateDto();

    // When
    Worker worker = workerService.update(1L, workerUpdateDto);

    // Then
    assertEquals(workerUpdateDto.getEmail(), worker.getEmail());
    assertEquals(workerUpdateDto.getName(), worker.getName());
    assertEquals(workerUpdateDto.getPhone(), worker.getPhone());
  }

  @Test
  void update_shouldThrowWorkerNotFoundException_whenWorkerIsNotFound() {
    // Given
    when(workerRepository.findById(anyLong())).thenReturn(Optional.empty());
    WorkerUpdateDto updateDto = createWorkerUpdateDto();

    // When
    WorkerNotFoundException ex = assertThrows(WorkerNotFoundException.class,
        () -> workerService.update(1L, updateDto));

    // Then
    assertThat(ex.getMessage(),
        containsString(format(WORKER_NOT_FOUND_MSG_TEMPLATE, 1L)));
  }

  @Test
  void update_shouldThrowWorkerAlreadyExistsException_whenWorkerAlreadyExists() {
    when(workerRepository.findById(any(Long.class))).thenReturn(Optional.of(new Worker()));
    when(workerRepository.findByEmail(any(String.class))).thenReturn(Optional.of(new Worker()));
    WorkerUpdateDto workerUpdateDto = createWorkerUpdateDto();

    // When
    WorkerAlreadyExistsException ex = assertThrows(WorkerAlreadyExistsException.class,
        () -> workerService.update(1L, workerUpdateDto));

    // Then
    assertThat(ex.getMessage(), containsString(format(WORKER_ALREADY_EXISTS_MSG_TEMPLATE, workerUpdateDto.getEmail())));
  }

  @Test
  void delete_isSuccessful_whenValidDataProvided() {
    // Given
    Worker worker = createWorker();
    when(workerRepository.findById(any(Long.class))).thenReturn(Optional.of(worker));

    // When
    workerService.delete(1L);

    // Then
    verify(workerRepository, times(1)).delete(worker);
  }

  @Test
  void delete_shouldThrowWorkerNotFoundException_whenWorkerIsNotFound() {
    // Given
    when(workerRepository.findById(anyLong())).thenReturn(Optional.empty());

    // When
    WorkerNotFoundException ex = assertThrows(WorkerNotFoundException.class,
        () -> workerService.delete(1L));

    // Then
    assertThat(ex.getMessage(), containsString(format(WORKER_NOT_FOUND_MSG_TEMPLATE, 1L)));
    verify(workerRepository, never()).delete(any(Worker.class));
  }

  private void saveWorkerStubbing() {
    when(workerRepository.save(any(Worker.class))).thenAnswer(invocation -> {
      Worker entity = (Worker) invocation.getArguments()[0];
      entity.setId(1L); // simulate the saving of the entity in the db and set ID
      return entity;
    });
  }
}
