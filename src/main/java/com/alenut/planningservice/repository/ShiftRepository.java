package com.alenut.planningservice.repository;

import com.alenut.planningservice.model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

}
