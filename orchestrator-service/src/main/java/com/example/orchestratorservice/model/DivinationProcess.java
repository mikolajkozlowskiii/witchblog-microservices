package com.example.orchestratorservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.common.model.DivinationProcessStatus;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;
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

    @Column
    private String divination;

    @Column
    @Enumerated(EnumType.STRING)
    private DivinationProcessStatus status = DivinationProcessStatus.Started;

    @Column
    private String statusComment;

    @Column
    @CreationTimestamp
    private Date createdAt;


}
