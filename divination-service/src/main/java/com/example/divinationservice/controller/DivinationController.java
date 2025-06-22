package com.example.divinationservice.controller;

import com.example.divinationservice.dto.DivinationGenerationResult;
import com.example.divinationservice.dto.DivinationRequestDTO;
import com.example.divinationservice.service.DivinationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/divination-service")
@RequiredArgsConstructor
@Slf4j
public class DivinationController {
    private final DivinationService divinationService;
    @PostMapping
    public ResponseEntity<DivinationGenerationResult> generateDivination(@RequestBody DivinationRequestDTO divinationRequestDTO) {
        return ResponseEntity.ok(divinationService.generateDivination(divinationRequestDTO));
    }

    @GetMapping("/divination/{divinationId}")
    public ResponseEntity<String> findDivinationByDivinationId(@PathVariable("divinationId") String divinationId) {
        return null;
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<String> findDivinationByUserId(@PathVariable("userId") String userId) {
        return null;
    }

    @GetMapping
    public ResponseEntity<String> findAllDivination() {
        return null;
    }

}
