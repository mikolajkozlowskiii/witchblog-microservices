package com.example.divinationservice.repository;

import com.example.divinationservice.model.DivinationProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DivinationProcessRepository extends JpaRepository<DivinationProcess, Long> {

    List<DivinationProcess> getDivinationProcessesByStatus(String status);

    Optional<DivinationProcess> getDivinationProcessByProcessId(UUID processId);
}

