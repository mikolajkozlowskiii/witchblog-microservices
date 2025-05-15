package com.example.orchestratorservice.component;


import com.example.orchestratorservice.model.Job;
import com.example.orchestratorservice.model.JobHistory;
import com.example.orchestratorservice.repository.JobHistoryRepository;
import com.example.orchestratorservice.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.common.event.PaymentCompletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentCompletedListener {
    private final JobRepository jobRepository;
    private final JobHistoryRepository historyRepository;

    @KafkaListener(topics = "PaymentCompletedTopic", groupId = "orchestrator-group")
    public void handle(PaymentCompletedEvent event) {
        Job job = jobRepository.findById(event.getJobId()).orElseThrow();
        job.setStatus("FINISHED");
        job.setFortuneResult("Twoja wróżba: będzie dobrze!!!!!!!! !");
        jobRepository.save(job);

        historyRepository.save(new JobHistory(null, job.getId(), "PaymentCompleted", LocalDateTime.now(), "Payment completed, fortune generated"));
    }
}