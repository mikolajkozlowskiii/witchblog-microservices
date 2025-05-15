package com.example.orchestratorservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class JobHistory {
    @Id
    @GeneratedValue
    private Long id;
    private String jobId;
    private String event;
    private LocalDateTime timestamp;
    private String details;
}