package com.example.divinationservice.service;

import com.example.divinationservice.dto.DivinationProcessDTO;
import com.example.divinationservice.dto.DivinationRequestDTO;
import com.example.divinationservice.dto.DivinationGenerationResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DivinationService {
    DivinationGenerationResult generateDivination(DivinationRequestDTO divinationRequestDTO);
    Optional<String> retryIntegration(UUID processId);
    List<DivinationProcessDTO> findDivinationProcessByUserId(UUID userId);
}
