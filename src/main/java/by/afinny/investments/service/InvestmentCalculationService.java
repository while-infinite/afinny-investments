package by.afinny.investments.service;


import by.afinny.investments.dto.ChangingPriceAssetDto;

import java.util.List;
import java.util.UUID;

public interface InvestmentCalculationService {

    List<ChangingPriceAssetDto> getExchangeRate(UUID brokerageAccountId, String authorization);
}
