package com.example.orchestratorservice.controller;

import com.example.orchestratorservice.model.DivinationProcess;
import com.example.orchestratorservice.repository.DivinationProcessRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.common.model.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("orchestrator-service")
public class ProcessController {
    private final DivinationProcessRepository divinationProcessRepository;
    private final ObjectMapper objectMapper;

    @PostMapping("/process/{userId}")
    public ResponseEntity<String> startProcess(@PathVariable("userId") String userId, @RequestBody UserInfo userInfo) throws JsonProcessingException {
        DivinationProcess divinationProcess = new DivinationProcess();
        divinationProcess.setUserId(UUID.fromString(userId));
        divinationProcess.setUserInfo(userInfo);
        divinationProcessRepository.save(divinationProcess);
        return ResponseEntity.ok(objectMapper.writeValueAsString(divinationProcess));
    }

    @GetMapping("/process/{userId}")
    public ResponseEntity<List<DivinationProcess>> getAllProcesses(@PathVariable("userId") String userId) {
        return divinationProcessRepository
                .findByUserId(UUID.fromString(userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/process/{userId}/{processId}")
        public ResponseEntity<DivinationProcess> getDivinationProcess(@PathVariable("userId") String userId, @PathVariable("processId") String processId) {
            return divinationProcessRepository
                    .findById(UUID.fromString(processId))
                    .filter(process -> Objects.equals(process.getUserId().toString(), userId))
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
}
