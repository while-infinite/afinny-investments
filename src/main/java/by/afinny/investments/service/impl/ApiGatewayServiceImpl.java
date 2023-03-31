package by.afinny.investments.service.impl;

import by.afinny.investments.dto.AvailableCurrenciesDto;
import by.afinny.investments.dto.AvailableStocksDto;
import by.afinny.investments.openfeign.apigateway.ApiGatewayClient;
import by.afinny.investments.service.ApiGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiGatewayServiceImpl implements ApiGatewayService {

    private final ApiGatewayClient apiGatewayClient;

    @Override
//    @Cacheable("availableStocksCache")
    public AvailableStocksDto.AllAvailableStocks getAvailableStocks(String str) {
        return apiGatewayClient.getAvailableStocks(str).getBody();
    }

    @Override
//    @Cacheable("availableCurrenciesCache")
    public AvailableCurrenciesDto.AllAvailableCurrencies getAvailableCurrencies(String str) {
        return apiGatewayClient.getAvailableCurrencies(str).getBody();
    }
}