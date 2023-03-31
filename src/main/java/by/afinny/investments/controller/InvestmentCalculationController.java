package by.afinny.investments.controller;

import by.afinny.investments.dto.ChangingPriceAssetDto;
import by.afinny.investments.service.InvestmentCalculationService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("auth/investment-calculations")
public class InvestmentCalculationController {

    private final InvestmentCalculationService investmentCalculationService;

    @GetMapping("/investment/exchange-rate")
    ResponseEntity<List<ChangingPriceAssetDto>> getExchangeRate(
            @RequestParam("brokerageAccountId") UUID brokerageAccountId, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok(investmentCalculationService.getExchangeRate(brokerageAccountId, authorization));
    }
}
