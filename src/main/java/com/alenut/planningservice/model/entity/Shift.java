package com.alenut.planningservice.model.entity;

import com.alenut.planningservice.model.enums.ShiftTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "shift")
public class Shift {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "worker_id", nullable = false)
  private Worker worker;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private ShiftTypeEnum type;

  @Column(name = "work_day", nullable = false)
  private LocalDate workDay;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Worker getWorker() {
    return worker;
  }

  public void setWorker(Worker worker) {
    this.worker = worker;
  }

  public ShiftTypeEnum getType() {
    return type;
  }

  public void setType(ShiftTypeEnum type) {
    this.type = type;
  }

  public LocalDate getWorkDay() {
    return workDay;
  }

  public void setWorkDay(LocalDate workDay) {
    this.workDay = workDay;
  }
}
