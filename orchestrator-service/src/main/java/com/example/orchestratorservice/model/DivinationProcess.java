package com.example.orchestratorservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.common.eventing.gpt.tarot.TarotCard;
import org.common.model.DivinationProcessStatus;
import org.common.model.PaymentState;
import org.common.model.UserInfo;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DivinationProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(columnDefinition = "text")
    private String divination;

    @Column
    @Enumerated(EnumType.STRING)
    private DivinationProcessStatus status = DivinationProcessStatus.Started;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentState paymentState;

    @ElementCollection
    private List<TarotCard> tarotCards = new ArrayList<>();

    @Embedded
    private UserInfo userInfo;

    @Column
    private String statusComment;

    @Column
    @CreationTimestamp
    private Date createdAt;


}
