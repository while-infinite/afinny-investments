package by.afinny.investments.service;

import by.afinny.investments.dto.DealExchangeRateDto;
import by.afinny.investments.dto.ResponseMoneySumDto;

import java.util.List;
import java.util.UUID;

public interface CalculationService {

    List<DealExchangeRateDto> getDealsForCalculation(UUID brokerageAccountId);

    ResponseMoneySumDto getMoneySum(UUID brokerageAccountId, String authorization);
}
