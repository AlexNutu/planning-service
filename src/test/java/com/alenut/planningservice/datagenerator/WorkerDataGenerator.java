package com.alenut.planningservice.datagenerator;

import com.alenut.planningservice.dto.WorkerBaseDto;
import com.alenut.planningservice.dto.WorkerUpdateDto;
import com.alenut.planningservice.model.entity.Worker;

public final class WorkerDataGenerator {

  private WorkerDataGenerator() {
  }

  public static WorkerBaseDto createWorkerBaseDto() {
    WorkerBaseDto baseDto = new WorkerBaseDto();
    baseDto.setEmail("worker.joe@gmail.com");
    baseDto.setName("John Worker");
    baseDto.setPhone("+49 123 456 789");
    return baseDto;
  }

  public static WorkerUpdateDto createWorkerUpdateDto() {
    WorkerUpdateDto updateDto = new WorkerUpdateDto();
    updateDto.setEmail("worker.dan@gmail.com");
    updateDto.setName("Dan Worker");
    updateDto.setPhone("+50 123 456 789");
    return updateDto;
  }

  public static Worker createWorker() {
    Worker entity = new Worker();
    entity.setId(1L);
    entity.setEmail("workerjoe@gmail.com");
    entity.setName("Joe Worker");
    entity.setPhone("+49 123 456 789");
    return entity;
  }
}
