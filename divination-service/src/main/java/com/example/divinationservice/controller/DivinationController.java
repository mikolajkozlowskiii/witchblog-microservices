package com.example.divinationservice.controller;

import com.example.divinationservice.dto.DivinationGenerationResult;
import com.example.divinationservice.dto.DivinationRequestDTO;
import com.example.divinationservice.service.DivinationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("divination-service")
@RequiredArgsConstructor
@Slf4j
public class DivinationController {
    private final DivinationService divinationService;
    @PostMapping
    public ResponseEntity<DivinationGenerationResult> generateDivination(@RequestBody DivinationRequestDTO divinationRequestDTO) {
        return ResponseEntity.ok(divinationService.generateDivination(divinationRequestDTO));
    }

    @PostMapping("/retry/{processId}")
    public ResponseEntity<String> retryIntegration(@PathVariable("processId") String processId){
        return divinationService
                .retryIntegration(UUID.fromString(processId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("integration failed"));
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
