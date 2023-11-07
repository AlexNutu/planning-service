package com.alenut.planningservice.controller;

import static com.alenut.planningservice.controller.WorkerController.WORKERS_PATH;
import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorkerBaseDto;
import static com.alenut.planningservice.datagenerator.WorkerDataGenerator.createWorkerUpdateDto;
import static com.alenut.planningservice.service.impl.WorkerServiceImpl.WORKER_NOT_FOUND_MSG_TEMPLATE;
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
import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerDto;
import com.alenut.planningservice.dto.WorkerUpdateDto;
import com.alenut.planningservice.model.entity.Worker;
import com.alenut.planningservice.service.WorkerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class WorkerControllerIntegrationTest extends AbstractIntegrationTest {

  private final WorkerService workerService;
  private final WorkerController workerController;
  private final ObjectMapper objectMapper;
  private MockMvc mockMvc;

  @Autowired
  public WorkerControllerIntegrationTest(WorkerService workerService, WorkerController workerController,
      ObjectMapper objectMapper) {
    this.workerService = workerService;
    this.workerController = workerController;
    this.objectMapper = objectMapper;
  }

  @BeforeEach
  public void init() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(workerController)
        .setControllerAdvice(new GlobalControllerAdvice())
        .build();
  }

  @Test
  void create_shouldReturn201_whenInputIsValid() throws Exception {
    // Given
    WorkerBaseDto workerBaseDto = createWorkerBaseDto();

    // When
    String responseString =
        this.mockMvc
            .perform(
                post(WORKERS_PATH)
                    .content(objectMapper.writeValueAsString(workerBaseDto))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(header().string(HttpHeaders.LOCATION, startsWith(WORKERS_PATH)))
            .andReturn()
            .getResponse()
            .getContentAsString();

    WorkerDto responseDto = objectMapper.readValue(responseString, WorkerDto.class);

    // Then
    assertNotNull(responseDto.getId());
    assertEquals(workerBaseDto.getEmail(), responseDto.getEmail());
    assertEquals(workerBaseDto.getName(), responseDto.getName());
    assertEquals(workerBaseDto.getPhone(), responseDto.getPhone());
  }

  @Test
  void create_shouldReturn400_whenInputIsInvalid() throws Exception {
    // Given
    WorkerBaseDto workerBaseDto = new WorkerBaseDto();

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                post(WORKERS_PATH)
                    .content(objectMapper.writeValueAsString(workerBaseDto))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertTrue(errorMsg.contains("name=must not be empty"));
    assertTrue(errorMsg.contains("email=must not be empty"));
  }

  @Test
  void getById_shouldReturn200_whenIdIsValid() throws Exception {
    // Given
    WorkerBaseDto workerBaseDto = createWorkerBaseDto();
    Worker worker = workerService.create(workerBaseDto);

    // When
    String responseString =
        this.mockMvc
            .perform(
                get(WORKERS_PATH + "/{id}", worker.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    WorkerDto responseDto = objectMapper.readValue(responseString, WorkerDto.class);

    // Then
    assertEquals(worker.getId(), responseDto.getId());
    assertEquals(worker.getEmail(), responseDto.getEmail());
    assertEquals(worker.getName(), responseDto.getName());
    assertEquals(worker.getPhone(), responseDto.getPhone());
  }

  @Test
  void getById_shouldReturn404_whenIdIsUnknown() throws Exception {
    // Given
    Long unknownWorkerId = -99L;

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                get(WORKERS_PATH + "/{id}", unknownWorkerId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertEquals(format("Error: " + WORKER_NOT_FOUND_MSG_TEMPLATE, unknownWorkerId), errorMsg);
  }

  @Test
  void update_shouldReturn200_whenInputIsValid() throws Exception {
    // Given
    WorkerBaseDto workerBaseDto = createWorkerBaseDto();
    Worker worker = workerService.create(workerBaseDto);
    WorkerUpdateDto updateDto = createWorkerUpdateDto();

    // When
    String responseString =
        this.mockMvc
            .perform(
                put(WORKERS_PATH + "/{id}", worker.getId())
                    .content(objectMapper.writeValueAsString(updateDto))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    WorkerDto responseDto = objectMapper.readValue(responseString, WorkerDto.class);

    assertEquals(updateDto.getEmail(), responseDto.getEmail());
    assertEquals(updateDto.getName(), responseDto.getName());
    assertEquals(updateDto.getPhone(), responseDto.getPhone());
  }

  @Test
  void update_shouldReturn404_whenIdIsUnknown() throws Exception {
    // Given
    Long unknownWorkerId = -99L;

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                put(WORKERS_PATH + "/{id}", unknownWorkerId)
                    .content(objectMapper.writeValueAsString(createWorkerUpdateDto()))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertEquals(format("Error: " + WORKER_NOT_FOUND_MSG_TEMPLATE, unknownWorkerId), errorMsg);
  }

  @Test
  void update_shouldReturn400_whenInputIsInvalid() throws Exception {
    // Given
    WorkerBaseDto workerBaseDto = createWorkerBaseDto();
    Worker worker = workerService.create(workerBaseDto);
    WorkerUpdateDto updateDto = new WorkerUpdateDto();

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                put(WORKERS_PATH + "/{id}", worker.getId())
                    .content(objectMapper.writeValueAsString(updateDto))
                    .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertTrue(errorMsg.contains("name=must not be empty"));
    assertTrue(errorMsg.contains("email=must not be empty"));
  }

  @Test
  void delete_returnsHttp204_whenWorkerIsDeleted() throws Exception {
    // Given
    WorkerBaseDto workerBaseDto = createWorkerBaseDto();
    Worker worker = workerService.create(workerBaseDto);

    this.mockMvc
        .perform(
            delete(WORKERS_PATH + "/{id}", worker.getId()))
        .andExpect(status().isNoContent());
  }

  @Test
  void delete_shouldReturn404_whenIdIsUnknown() throws Exception {
    // Given
    Long unknownWorkerId = -99L;

    // When
    String errorMsg =
        this.mockMvc
            .perform(
                delete(WORKERS_PATH + "/{id}", unknownWorkerId))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    assertEquals(format("Error: " + WORKER_NOT_FOUND_MSG_TEMPLATE, unknownWorkerId), errorMsg);
  }
}
