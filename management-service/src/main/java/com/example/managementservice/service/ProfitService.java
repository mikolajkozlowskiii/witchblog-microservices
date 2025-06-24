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

    @Value("${token.cost.per.million:2}")
    private double tokenCostPerMillion;


    public ProfitDTO calculateProfit(Date startDate, Date endDate) {
        if (startDate == null) startDate = Date.from(LocalDate.of(1, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant());
        if (endDate == null) endDate = Date.from(LocalDate.of(9999, 12, 31).atStartOfDay(ZoneOffset.UTC).toInstant());


        double totalPayments = paymentRepository.sumPaymentsBetweenDates(startDate, endDate);
        Long totalUsedTokens = promptCostRepository.sumUsedTokensBetweenDates(startDate, endDate);

        if (totalUsedTokens == null) {
            totalUsedTokens = 0L;
        }

        double tokenCost = (totalUsedTokens / 1_000_000.0) * tokenCostPerMillion;
        double profit = totalPayments - tokenCost;

        return ProfitDTO
                .builder()
                .profit(profit)
                .totalPayments(totalPayments)
                .totalUsedTokens(totalUsedTokens)
                .build();
    }
}
