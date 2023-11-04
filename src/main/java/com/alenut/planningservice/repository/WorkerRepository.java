package com.alenut.planningservice.repository;

import com.alenut.planningservice.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

}
