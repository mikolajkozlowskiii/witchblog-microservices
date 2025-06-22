package com.example.divinationservice.service;

import com.example.divinationservice.dto.DivinationRequestDTO;
import com.example.divinationservice.dto.DivinationGenerationResult;

public interface DivinationService {
    DivinationGenerationResult generateDivination(DivinationRequestDTO divinationRequestDTO);
}
