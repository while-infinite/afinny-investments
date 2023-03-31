package by.afinny.investments.controller;

import by.afinny.investments.dto.DealExchangeRateDto;
import by.afinny.investments.dto.ResponseMoneySumDto;
import by.afinny.investments.service.CalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("auth/investment-calculations")
@RequiredArgsConstructor
@Slf4j
public class CalculationController {

    private final CalculationService calculationService;

    @GetMapping("/investment")
    public ResponseEntity<List<DealExchangeRateDto>> getDealsForCalculation(
            @RequestParam("brokerageAccountId") UUID brokerageAccountId) {
        return ResponseEntity.ok(calculationService.getDealsForCalculation(brokerageAccountId));
    }

    @GetMapping("/investment-sum")
    public ResponseEntity<ResponseMoneySumDto> getMoneySum(@RequestParam UUID brokerageAccountId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok(calculationService.getMoneySum(brokerageAccountId, authorization));
    }

}