package com.example.divinationservice.dto;


public record DivinationGenerationResult(DivinationResponseDTO responseDTO, String prompt, String modelName) {}