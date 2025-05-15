package com.example.orchestratorservice.component;

import lombok.RequiredArgsConstructor;
import org.common.event.PaymentSessionCreatedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PaymentSessionWaiter {
    private final Map<String, CompletableFuture<PaymentSessionCreatedEvent>> futures = new ConcurrentHashMap<>();

    public CompletableFuture<PaymentSessionCreatedEvent> register(String jobId) {
        CompletableFuture<PaymentSessionCreatedEvent> future = new CompletableFuture<>();
        futures.put(jobId, future);
        return future;
    }

    public void complete(String jobId, PaymentSessionCreatedEvent event) {
        CompletableFuture<PaymentSessionCreatedEvent> future = futures.remove(jobId);
        if (future != null) {
            future.complete(event);
        }
    }
}