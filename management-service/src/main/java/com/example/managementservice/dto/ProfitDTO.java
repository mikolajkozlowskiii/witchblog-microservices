package com.example.managementservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProfitDTO {
    private double profit;
    private long totalUsedTokens;
    private double totalPayments;
}
