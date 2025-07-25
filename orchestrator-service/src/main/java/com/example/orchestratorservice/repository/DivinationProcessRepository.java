package com.example.orchestratorservice.repository;

import com.example.orchestratorservice.model.DivinationProcess;
import org.common.model.DivinationProcessStatus;
import org.common.model.PaymentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DivinationProcessRepository extends JpaRepository<DivinationProcess, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE DivinationProcess d SET d.status = :status WHERE d.id = :id")
    void updateStatusById(@Param("id") UUID id, @Param("status") DivinationProcessStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE DivinationProcess d SET d.paymentState = :status WHERE d.id = :id")
    void updatePaymentStatusById(@Param("id") UUID id, @Param("status") PaymentState status);

    Optional<List<DivinationProcess>> findByUserId(UUID userId);

    Optional<DivinationProcess> findById(UUID id);
}
