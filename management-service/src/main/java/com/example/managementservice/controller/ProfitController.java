package com.example.managementservice.controller;

import com.example.managementservice.service.ProfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("management-service")
@RequiredArgsConstructor
public class ProfitController {

    private final ProfitService profitService;

    @GetMapping("/profit")
    public double getProfit(
            @RequestParam(name = "startDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        return profitService.calculateProfit(startDate, endDate);
    }
}
