package com.alenut.planningservice.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "worker")
public class Worker {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "worker", cascade = CascadeType.ALL)
  private List<Shift> shifts;

  @Column(name = "email", nullable = false, unique = true, length = 100)
  private String email;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "phone", length = 15)
  private String phone;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<Shift> getShifts() {
    return shifts;
  }

  public void setShifts(List<Shift> shifts) {
    this.shifts = shifts;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
