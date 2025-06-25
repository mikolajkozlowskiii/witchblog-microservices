package com.example.managementservice.controller;

import com.example.managementservice.dto.ProfitDTO;
import com.example.managementservice.service.ProfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("management-service")
@RequiredArgsConstructor
public class ProfitController {

    private final ProfitService profitService;

    @GetMapping("/profit")
    public ResponseEntity<ProfitDTO> getProfit(
            @RequestHeader(value = "X-Admin-Password", required = false) String adminPassword,
            @RequestParam(name = "startDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        if(!"tarot123".equals(adminPassword)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        ProfitDTO response = profitService.calculateProfit(startDate, endDate);

        return ResponseEntity.ok(response);
    }
}
