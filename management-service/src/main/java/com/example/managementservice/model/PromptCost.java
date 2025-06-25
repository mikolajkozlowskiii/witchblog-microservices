package com.example.managementservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PromptCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer usedInputTokens;

    @Column
    private Integer usedOutputTokens;

    @Column
    @CreationTimestamp
    private Date createdAt;
}
