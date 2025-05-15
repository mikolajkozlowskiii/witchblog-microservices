package com.example.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.event.PaymentCompletedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment-service")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    @GetMapping("/update/{jobId}")
    public ResponseEntity<String> findAvailableProductById(@PathVariable("jobId") String jobId){
        return ResponseEntity.ok("updated " + jobId);
    }
    @PostMapping("/webhook/{jobId}")
    public void completePayment(@PathVariable String jobId) {
        PaymentCompletedEvent completedEvent = new PaymentCompletedEvent(jobId);
        kafkaTemplate.send("PaymentCompletedTopic", completedEvent);
        log.info("Sent PaymentCompletedEvent for jobId: " + jobId);
    }
}
