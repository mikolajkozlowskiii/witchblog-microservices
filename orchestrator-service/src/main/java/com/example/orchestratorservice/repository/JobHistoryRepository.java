package com.example.orchestratorservice.repository;

import com.example.orchestratorservice.model.JobHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobHistoryRepository extends JpaRepository<JobHistory, Long> {}
