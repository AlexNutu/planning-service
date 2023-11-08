package com.alenut.planningservice.service;

import static com.alenut.planningservice.datagenerator.ShiftDataGenerator.createShift;
import static com.alenut.planningservice.datagenerator.ShiftDataGenerator.createShiftBaseDto;
import static com.alenut.planningservice.datagenerator.ShiftDataGenerator.createShiftUpdateDto;
import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorker;
import static com.alenut.planningservice.service.impl.ShiftServiceImpl.SHIFT_ALREADY_EXISTS_MSG_TEMPLATE;
import static com.alenut.planningservice.service.impl.ShiftServiceImpl.SHIFT_NOT_FOUND_MSG_TEMPLATE;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.alenut.planningservice.common.exception.ShiftAlreadyExistsException;
import com.alenut.planningservice.common.exception.ShiftNotFoundException;
import com.alenut.planningservice.common.exception.WorkerNotFoundException;
import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftUpdateDto;
import com.alenut.planningservice.model.entity.Shift;
import com.alenut.planningservice.model.entity.Worker;
import com.alenut.planningservice.model.enums.ShiftTypeEnum;
import com.alenut.planningservice.repository.ShiftRepository;
import com.alenut.planningservice.service.impl.ShiftServiceImpl;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ShiftServiceTest {

  @InjectMocks
  private ShiftServiceImpl shiftService;

  @Mock
  private ShiftRepository shiftRepository;
  @Mock
  private WorkerService workerService;

  @BeforeEach
  void init() {
    openMocks(this);
  }

  @Test
  void create_isSuccessful_whenValidDataProvided() {
    // Given
    Worker worker = createWorker();
    ShiftBaseDto shiftBaseDto = createShiftBaseDto(worker.getId());
    when(shiftRepository.findByWorkerIdAndWorkDay(any(Long.class), any(LocalDate.class))).thenReturn(Optional.empty());
    when(workerService.getById(any(Long.class))).thenReturn(worker);
    saveShiftStubbing();

    // When
    Shift shift = shiftService.create(shiftBaseDto);

    // Then
    assertEquals(1L, shift.getId());
    assertEquals(shiftBaseDto.getWorkerId(), shift.getWorker().getId());
    assertEquals(shiftBaseDto.getType(), shift.getType());
    assertEquals(shiftBaseDto.getWorkDay(), shift.getWorkDay());
  }

  @Test
  void create_shouldThrowWorkerNotFoundException_whenWorkerIdDoesNotExist() {
    // Given
    ShiftBaseDto shiftBaseDto = createShiftBaseDto(-1L);
    when(workerService.getById(-1L)).thenThrow(WorkerNotFoundException.class);

    // When
    assertThrows(WorkerNotFoundException.class, () -> shiftService.create(shiftBaseDto));
  }

  @Test
  void create_shouldThrowShiftAlreadyExistsException_whenShiftAlreadyExists() {
    // Given
    Worker worker = createWorker();
    Shift shift = createShift(worker.getId());
    ShiftBaseDto shiftBaseDto = createShiftBaseDto(worker.getId());
    when(workerService.getById(any(Long.class))).thenReturn(worker);
    when(shiftRepository.findByWorkerIdAndWorkDay(any(Long.class), any(LocalDate.class))).thenReturn(
        Optional.of(shift));

    // When
    ShiftAlreadyExistsException ex = assertThrows(ShiftAlreadyExistsException.class,
        () -> shiftService.create(shiftBaseDto));

    // Then
    assertEquals(format(SHIFT_ALREADY_EXISTS_MSG_TEMPLATE, worker.getId(), shiftBaseDto.getWorkDay(),
        shiftBaseDto.getType().getStartHour() + "-" + shiftBaseDto.getType().getEndHour()), ex.getMessage());
  }

  @Test
  void getById_isSuccessful_whenDataIsFound() {
    // Given
    Worker worker = createWorker();
    Shift entity = createShift(worker.getId());
    when(shiftRepository.findById(any(Long.class))).thenReturn(Optional.of(entity));

    // When
    Shift shift = shiftService.getById(1L);

    // Then
    assertEquals(worker.getId(), shift.getWorker().getId());
    assertEquals(entity.getId(), shift.getId());
    assertEquals(entity.getType(), shift.getType());
    assertEquals(entity.getWorkDay(), shift.getWorkDay());
  }

  @Test
  void getById_shouldThrowShiftNotFoundException_whenShiftIdIsUnknown() {
    // Given
    when(shiftRepository.findById(any(Long.class))).thenReturn(Optional.empty());

    // When
    ShiftNotFoundException exc =
        assertThrows(ShiftNotFoundException.class, () -> shiftService.getById(-1L));

    // Then
    assertEquals(format(SHIFT_NOT_FOUND_MSG_TEMPLATE, -1L), exc.getMessage());
  }

  @Test
  void findByWorkerId_isSuccessful_whenDataIsFound() {
    // Given
    Worker worker = createWorker();
    Shift morningShift = createShift(worker.getId());
    Shift nightShift = createShift(worker.getId());
    nightShift.setType(ShiftTypeEnum.NIGHT);
    nightShift.setWorkDay(LocalDate.parse("2024-01-02"));
    when(shiftRepository.findByWorkerId(any(Long.class))).thenReturn(List.of(morningShift, nightShift));

    // When
    List<Shift> foundShifts = shiftService.findByWorkerId(worker.getId());

    // Then
    assertEquals(2, foundShifts.size());
    assertEquals(morningShift.getType(), foundShifts.get(0).getType());
    assertEquals(nightShift.getType(), foundShifts.get(1).getType());
  }

  @Test
  void findByWorkerId_shouldReturnEmptyList_whenShiftWithGivenWorkerIdDoesNotExists() {
    // Given
    when(shiftRepository.findByWorkerId(any(Long.class))).thenReturn(Collections.emptyList());

    // When
    List<Shift> foundShifts = shiftService.findByWorkerId(-1L);

    // Then
    assertTrue(foundShifts.isEmpty());
  }

  @Test
  void update_isSuccessful_whenValidDataProvided() {
    // Given
    Worker worker = createWorker();
    Shift entity = createShift(worker.getId());
    when(shiftRepository.findById(any(Long.class))).thenReturn(Optional.of(entity));
    when(shiftRepository.findByWorkerIdAndWorkDay(any(Long.class), any(LocalDate.class))).thenReturn(Optional.empty());
    saveShiftStubbing();
    ShiftUpdateDto shiftUpdateDto = createShiftUpdateDto();

    // When
    Shift shift = shiftService.update(entity.getId(), shiftUpdateDto);

    // Then
    assertEquals(shiftUpdateDto.getType(), shift.getType());
    assertEquals(shiftUpdateDto.getWorkDay(), shift.getWorkDay());
  }

  @Test
  void update_shouldThrowShiftNotFoundException_whenShiftIsNotFound() {
    // Given
    when(shiftRepository.findById(anyLong())).thenReturn(Optional.empty());
    ShiftUpdateDto shiftUpdateDto = createShiftUpdateDto();

    // When
    ShiftNotFoundException ex = assertThrows(ShiftNotFoundException.class,
        () -> shiftService.update(-1L, shiftUpdateDto));

    // Then
    assertEquals(format(SHIFT_NOT_FOUND_MSG_TEMPLATE, -1L), ex.getMessage());
  }

  @Test
  void update_shouldThrowShiftAlreadyExistsException_whenShiftAlreadyExists() {
    // Given
    Worker worker = createWorker();
    Shift shift = createShift(worker.getId());
    when(shiftRepository.findById(any(Long.class))).thenReturn(Optional.of(shift));
    when(shiftRepository.findByWorkerIdAndWorkDay(any(Long.class), any(LocalDate.class))).thenReturn(
        Optional.of(shift));
    ShiftUpdateDto shiftUpdateDto = createShiftUpdateDto();

    // When
    Long shiftId = shift.getId();
    ShiftAlreadyExistsException ex = assertThrows(ShiftAlreadyExistsException.class,
        () -> shiftService.update(shiftId, shiftUpdateDto));

    // Then
    assertEquals(format(SHIFT_ALREADY_EXISTS_MSG_TEMPLATE, worker.getId(), shiftUpdateDto.getWorkDay(),
        shift.getType().getStartHour() + "-" + shift.getType().getEndHour()), ex.getMessage());
  }

  @Test
  void delete_isSuccessful_whenValidDataProvided() {
    // Given
    Worker worker = createWorker();
    Shift shift = createShift(worker.getId());
    when(shiftRepository.findById(any(Long.class))).thenReturn(Optional.of(shift));

    // When
    shiftService.delete(shift.getId());

    // Then
    verify(shiftRepository, times(1)).delete(shift);
  }

  @Test
  void delete_shouldThrowShiftNotFoundException_whenShiftIsNotFound() {
    // Given
    when(shiftRepository.findById(anyLong())).thenReturn(Optional.empty());

    // When
    ShiftNotFoundException ex = assertThrows(ShiftNotFoundException.class,
        () -> shiftService.delete(1L));

    // Then
    assertThat(ex.getMessage(), containsString(format(SHIFT_NOT_FOUND_MSG_TEMPLATE, 1L)));
    verify(shiftRepository, never()).delete(any(Shift.class));
  }

  private void saveShiftStubbing() {
    when(shiftRepository.save(any(Shift.class))).thenAnswer(invocation -> {
      Shift entity = (Shift) invocation.getArguments()[0];
      entity.setId(1L); // simulate the saving of the entity in the db and set ID
      return entity;
    });
  }
}