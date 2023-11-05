package com.alenut.planningservice.repository;

import com.alenut.planningservice.model.entity.Worker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

  Optional<Worker> findByEmail(String email);
}
