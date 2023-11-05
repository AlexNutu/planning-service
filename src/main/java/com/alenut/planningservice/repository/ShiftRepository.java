package com.alenut.planningservice.repository;

import com.alenut.planningservice.model.entity.Shift;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

  Optional<Shift> findByWorkerIdAndWorkDay(Long workerId, LocalDate workDay);
}
