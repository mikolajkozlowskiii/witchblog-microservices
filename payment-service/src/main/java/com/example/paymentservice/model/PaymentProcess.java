package com.example.paymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.common.model.PaymentState;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaymentProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private UUID divinationId;

    @Column
    private UUID userId;

    @Column
    private String status;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentState paymentState = PaymentState.PENDING;

    @Column
    private float amount = 1.0f;


}
