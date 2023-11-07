package com.alenut.planningservice.controller;

import static com.alenut.planningservice.controller.ShiftController.SHIFTS_PATH;
import static com.alenut.planningservice.datagenerator.ShiftDataGenerator.createShiftBaseDto;
import static com.alenut.planningservice.datagenerator.ShiftDataGenerator.createShiftUpdateDto;
import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorkerBaseDto;
import static com.alenut.planningservice.service.impl.ShiftServiceImpl.SHIFT_NOT_FOUND_MSG_TEMPLATE;
import static java.lang.String.format;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alenut.planningservice.common.AbstractIntegrationTest;
import com.alenut.planningservice.config.GlobalControllerAdvice;
import com.alenut.planningservice.dto.ShiftBaseDto;
import com.alenut.planningservice.dto.ShiftDto;
import com.alenut.planningservice.dto.ShiftUpdateDto;
import com.alenut.planningservice.model.entity.Shift;
import com.alenut.planningservice.model.entity.Worker;
import com.alenut.planningservice.service.ShiftService;
import com.alenut.planningservice.service.WorkerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ShiftControllerIntegrationTest extends AbstractIntegrationTest {

  private final WorkerService workerService;
  private final ShiftService shiftService;
  private final ShiftController shiftController;
  private final ObjectMapper objectMapper;
  private MockMvc mockMvc;

  @Autowired
  public ShiftControllerIntegrationTest(WorkerService workerService, ShiftService shiftService,
      ShiftController shiftController, ObjectMapper objectMapper) {
    this.workerService = workerService;
    this.shiftService = shiftService;
    this.shiftController = shiftController;
    this.objectMapper = objectMapper;
  }

  @BeforeEach
  public void init() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(shiftController)
        .setControllerAdvice(new GlobalControllerAdvice())
        .build();
  }

  @Test
  void create_shouldReturn201_whenInputIsValid() throws Exception {
    // Given
    Worker worker = workerService.create(createWorkerBaseDto());
    ShiftBaseDto shiftBaseDto = createShiftBaseDto(worker.getId());

    // When
    String responseString =
        this.mockMvc
            .perform(
                post(SHIFTS_PATH)
                    .content(objectMapper.writeValueAsString(shiftBaseDto))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(header().string(HttpHeaders.LOCATION, startsWith(SHIFTS_PATH)))
            .andReturn()
            .getResponse()
            .getContentAsString();

    ShiftDto responseDto = objectMapper.readValue(responseString, ShiftDto.class);

    // Then
    assertNotNull(responseDto.getId());
    assertEquals(shiftBaseDto.getWorkerId(), responseDto.getWorkerId());
    assertEquals(shiftBaseDto.getType(), responseDto.getType());
    assertEquals(shiftBaseDto.getWorkDay(), responseDto.getWorkDay());
  }

  @Test
  void create_shouldReturn400_whenInputIsInvalid() throws Exception {
    // Given
    ShiftBaseDto shiftBaseDto = new ShiftBaseDto();

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                post(SHIFTS_PATH)
                    .content(objectMapper.writeValueAsString(shiftBaseDto))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertTrue(errorMsg.contains("workerId=must not be null"));
    assertTrue(errorMsg.contains("type=must not be null"));
    assertTrue(errorMsg.contains("workDay=must not be null"));
  }

  @Test
  void getById_shouldReturn200_whenIdIsValid() throws Exception {
    // Given
    Worker worker = workerService.create(createWorkerBaseDto());
    ShiftBaseDto shiftBaseDto = createShiftBaseDto(worker.getId());
    Shift shift = shiftService.create(shiftBaseDto);

    // When
    String responseString =
        this.mockMvc
            .perform(
                get(SHIFTS_PATH + "/{id}", shift.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    ShiftDto responseDto = objectMapper.readValue(responseString, ShiftDto.class);

    // Then
    assertEquals(worker.getId(), responseDto.getWorkerId());
    assertEquals(shift.getId(), responseDto.getId());
    assertEquals(shift.getType(), responseDto.getType());
    assertEquals(shift.getWorkDay(), responseDto.getWorkDay());
  }

  @Test
  void getById_shouldReturn404_whenIdIsUnknown() throws Exception {
    // Given
    Long unknownShiftId = -99L;

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                get(SHIFTS_PATH + "/{id}", unknownShiftId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertEquals(format("Error: " + SHIFT_NOT_FOUND_MSG_TEMPLATE, unknownShiftId), errorMsg);
  }

  @Test
  void update_shouldReturn200_whenInputIsValid() throws Exception {
    // Given
    Worker worker = workerService.create(createWorkerBaseDto());
    ShiftBaseDto shiftBaseDto = createShiftBaseDto(worker.getId());
    Shift shift = shiftService.create(shiftBaseDto);
    ShiftUpdateDto updateDto = createShiftUpdateDto();

    // When
    String responseString =
        this.mockMvc
            .perform(
                put(SHIFTS_PATH + "/{id}", shift.getId())
                    .content(objectMapper.writeValueAsString(updateDto))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    ShiftDto responseDto = objectMapper.readValue(responseString, ShiftDto.class);

    assertEquals(updateDto.getType(), responseDto.getType());
    assertEquals(updateDto.getWorkDay(), responseDto.getWorkDay());
  }

  @Test
  void update_shouldReturn404_whenIdIsUnknown() throws Exception {
    // Given
    Long unknownShiftId = -99L;

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                put(SHIFTS_PATH + "/{id}", unknownShiftId)
                    .content(objectMapper.writeValueAsString(createShiftUpdateDto()))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertEquals(format("Error: " + SHIFT_NOT_FOUND_MSG_TEMPLATE, unknownShiftId), errorMsg);
  }

  @Test
  void update_shouldReturn400_whenInputIsInvalid() throws Exception {
    // Given
    Worker worker = workerService.create(createWorkerBaseDto());
    ShiftBaseDto shiftBaseDto = createShiftBaseDto(worker.getId());
    Shift shift = shiftService.create(shiftBaseDto);
    ShiftUpdateDto updateDto = new ShiftUpdateDto();

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                put(SHIFTS_PATH + "/{id}", shift.getId())
                    .content(objectMapper.writeValueAsString(updateDto))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertTrue(errorMsg.contains("type=must not be null"));
    assertTrue(errorMsg.contains("workDay=must not be null"));
  }

  @Test
  void delete_returnsHttp204_whenShiftIsDeleted() throws Exception {
    // Given
    Worker worker = workerService.create(createWorkerBaseDto());
    ShiftBaseDto shiftBaseDto = createShiftBaseDto(worker.getId());
    Shift shift = shiftService.create(shiftBaseDto);

    this.mockMvc
        .perform(
            delete(SHIFTS_PATH + "/{id}", shift.getId()))
        .andExpect(status().isNoContent());
  }

  @Test
  void delete_shouldReturn404_whenIdIsUnknown() throws Exception {
    // Given
    Long unknownShiftId = -99L;

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                delete(SHIFTS_PATH + "/{id}", unknownShiftId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertEquals(format("Error: " + SHIFT_NOT_FOUND_MSG_TEMPLATE, unknownShiftId), errorMsg);
  }
}