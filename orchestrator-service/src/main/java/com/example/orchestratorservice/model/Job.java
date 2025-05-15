package com.example.orchestratorservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Job {
    @Id
    private String id;
    private String status;
    private String fortuneResult;
    private LocalDateTime createdAt;
}