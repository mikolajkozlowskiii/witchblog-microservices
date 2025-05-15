package com.example.orchestratorservice.repository;

import com.example.orchestratorservice.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, String> {}

