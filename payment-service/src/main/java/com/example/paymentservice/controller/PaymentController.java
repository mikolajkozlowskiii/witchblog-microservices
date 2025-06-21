package com.example.paymentservice.controller;

import com.example.paymentservice.blik.BLIKClient;
import com.example.paymentservice.dto.BLIKRequestDTO;
import com.example.paymentservice.repository.PaymentProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.eventing.core.model.Event;
import org.common.eventing.payment.event.IncorrectBLIKCodeEvent;
import org.common.model.PaymentState;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;


@RestController
@RequestMapping("/payment-service")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final BLIKClient blikClient;
    private final PaymentProcessRepository paymentProcessRepository;

    @PostMapping("/blik")
    public ResponseEntity<String> sendBLIKCode(@RequestBody BLIKRequestDTO request) {
        paymentProcessRepository
                .findByDivinationIdAndUserId(UUID.fromString(request.processId()), UUID.fromString(request.userId()))
                .filter(paymentProcess -> paymentProcess.getPaymentState().equals(PaymentState.PENDING))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No payment process found in correct state for divination id: " + request.processId() + " and user id: " + request.userId()));

        String code = request.BLIKCode();
        if(code == null || code.length() != 6 || !code.matches("\\d+")) {
            kafkaTemplate.send("frontend", new IncorrectBLIKCodeEvent("Incorrect BLIK code!", request.processId(), request.userId()));
            return ResponseEntity.badRequest().body("Invalid BLIK code");
        }

        blikClient.payWithBLIK(code, request.userId(),request.processId());
        return ResponseEntity.ok().build();
    }
}
