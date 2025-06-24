package com.example.managementservice.handler;

import com.example.managementservice.model.PromptCost;
import com.example.managementservice.repository.PromptCostRepository;
import lombok.AllArgsConstructor;
import org.common.eventing.core.handler.EventHandler;
import org.common.eventing.gpt.event.SendTokenConsumptionInfoEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SendTokenConsumptionInfoEventHandler implements EventHandler<SendTokenConsumptionInfoEvent> {

    private final PromptCostRepository promptCostRepository;

    @Override
    public void handle(SendTokenConsumptionInfoEvent event) {
        PromptCost cost = new PromptCost();
        cost.setUsedTokens(event.tokenConsumption());
        promptCostRepository.save(cost);
    }
}
