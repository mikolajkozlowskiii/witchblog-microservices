package com.example.managementservice.service;

import com.example.managementservice.dto.ProfitDTO;
import com.example.managementservice.repository.PaymentRepository;
import com.example.managementservice.repository.PromptCostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProfitService {

    private final PaymentRepository paymentRepository;
    private final PromptCostRepository promptCostRepository;

    @Value("${token.input.cost.per.million:1.1}")
    private double inputTokenCostPerMillion;
    @Value("${token.output.cost.per.million:4.4}")
    private double outputTokenCostPerMillion;


    public ProfitDTO calculateProfit(Date startDate, Date endDate) {
        if (startDate == null) startDate = Date.from(LocalDate.of(1, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant());
        if (endDate == null) endDate = Date.from(LocalDate.of(9999, 12, 31).atStartOfDay(ZoneOffset.UTC).toInstant());


        double totalPayments = paymentRepository.sumPaymentsBetweenDates(startDate, endDate);
        Long totalUsedInputTokens = promptCostRepository.sumUsedInputTokensBetweenDates(startDate, endDate);
        Long totalUsedOutputTokens = promptCostRepository.sumUsedTokensOutputBetweenDates(startDate, endDate);
        Long totalUsedTokens = totalUsedInputTokens + totalUsedOutputTokens;

        double tokenCost = (totalUsedInputTokens / 1_000_000.0) * inputTokenCostPerMillion + (totalUsedOutputTokens / 1_000_000.0) * outputTokenCostPerMillion;
        double profit = totalPayments - tokenCost;

        return ProfitDTO
                .builder()
                .profit(profit)
                .totalPayments(totalPayments)
                .totalUsedTokens(totalUsedTokens)
                .build();
    }
}
