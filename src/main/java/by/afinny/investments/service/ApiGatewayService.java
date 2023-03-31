package by.afinny.investments.service;

import by.afinny.investments.dto.AvailableCurrenciesDto;
import by.afinny.investments.dto.AvailableStocksDto;

public interface ApiGatewayService {

    AvailableStocksDto.AllAvailableStocks getAvailableStocks(String str);
    AvailableCurrenciesDto.AllAvailableCurrencies getAvailableCurrencies(String str);
}
