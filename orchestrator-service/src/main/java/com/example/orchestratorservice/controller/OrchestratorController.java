package com.example.orchestratorservice.controller;

import com.example.orchestratorservice.component.PaymentSessionWaiter;
import com.example.orchestratorservice.dto.JobStatus;
import com.example.orchestratorservice.model.Job;
import com.example.orchestratorservice.model.JobHistory;
import com.example.orchestratorservice.repository.JobHistoryRepository;
import com.example.orchestratorservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.common.dto.JobRequest;
import org.common.dto.JobResponse;
import org.common.event.PaymentRequestEvent;
import org.common.event.PaymentSessionCreatedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("orchestrator-service")
public class OrchestratorController {
    private final KafkaTemplate<String, PaymentRequestEvent> kafkaTemplate;
    private final PaymentSessionWaiter waiter;
    private final JobRepository jobRepository;
    private final JobHistoryRepository historyRepository;
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("test");
    }

    @PostMapping("/job")
    public ResponseEntity<JobResponse> createJob(@RequestBody JobRequest request) throws Exception {
        String jobId = UUID.randomUUID().toString();

        Job job = new Job();
        job.setId(jobId);
        job.setStatus("WAITING_FOR_PAYMENT");
        job.setCreatedAt(LocalDateTime.now());
        jobRepository.save(job);

        historyRepository.save(new JobHistory(null, jobId, "JobCreated", LocalDateTime.now(), "Created, waiting for payment"));

        kafkaTemplate.send("PaymentRequestTopic", new PaymentRequestEvent(jobId));

        CompletableFuture<PaymentSessionCreatedEvent> future = waiter.register(jobId);
        PaymentSessionCreatedEvent sessionEvent = future.get(1000, TimeUnit.SECONDS);

        return ResponseEntity.ok(new JobResponse(jobId, sessionEvent.getPaymentSessionUrl()));
    }

    @GetMapping("/status/{jobId}")
    public ResponseEntity<JobStatus> getStatus(@PathVariable String jobId) {
        return jobRepository.findById(jobId)
                .map(s-> JobStatus.builder().status(s.getStatus()).createdAt(s.getCreatedAt()).build())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/divination/{jobId}")
    public ResponseEntity<Job> getDivination(@PathVariable String jobId) {
        return jobRepository.findById(jobId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
