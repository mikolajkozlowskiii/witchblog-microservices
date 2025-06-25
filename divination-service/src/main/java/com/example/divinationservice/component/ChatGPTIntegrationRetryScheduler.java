package com.example.divinationservice.component;

import com.example.divinationservice.model.DivinationProcess;
import com.example.divinationservice.repository.DivinationProcessRepository;
import com.example.divinationservice.service.DivinationService;
import lombok.AllArgsConstructor;
import org.common.model.DivinationGenerationStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component //turned off for safety reasons
@AllArgsConstructor
public class ChatGPTIntegrationRetryScheduler {

    private final DivinationProcessRepository divinationProcessRepository;
    private final DivinationService divinationService;

    @Scheduled(fixedRate = 300_000) //5 minut
    public void retry() {
        List<DivinationProcess> processes = divinationProcessRepository
                .getDivinationProcessesByStatus(DivinationGenerationStatus.FAILURE.name());

        processes.forEach(process -> divinationService.retryIntegration(process.getProcessId()));

    }
}
