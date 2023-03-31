package by.afinny.investments.openfeign.apigateway;

import by.afinny.investments.dto.AvailableCurrenciesDto;
import by.afinny.investments.dto.AvailableStocksDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("API-GATEWAY/api/v1/investment")
public interface ApiGatewayClient {

    @GetMapping("/address")
    ResponseEntity<AvailableStocksDto.AllAvailableStocks> getAvailableStocks(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);


    @GetMapping("/currency")
    ResponseEntity<AvailableCurrenciesDto.AllAvailableCurrencies> getAvailableCurrencies(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
}
