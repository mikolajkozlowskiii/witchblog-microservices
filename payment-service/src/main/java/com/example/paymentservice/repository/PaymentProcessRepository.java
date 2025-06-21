package com.example.paymentservice.repository;

import com.example.paymentservice.model.PaymentProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentProcessRepository extends JpaRepository<PaymentProcess, UUID> {
    Optional<PaymentProcess> findByDivinationIdAndUserId(UUID divinationId, UUID userId);
}
