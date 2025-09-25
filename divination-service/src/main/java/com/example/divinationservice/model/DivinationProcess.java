package com.example.divinationservice.model;

import jakarta.persistence.*;
import org.common.eventing.gpt.tarot.TarotCard;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DivinationProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private UUID divinationId;

    @Column
    private UUID processId;

    @ElementCollection
    private List<TarotCard> tarotCards = new ArrayList<>();

    @Column
    private UUID userId;

    @Column
    private String status;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column
    private String llmModel;

    @Column(columnDefinition = "TEXT")
    private String response;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
