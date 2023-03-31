package by.afinny.investments.service.impl;

import by.afinny.investments.dto.AvailableCurrenciesDto;
import by.afinny.investments.dto.ChangingPriceAssetDto;
import by.afinny.investments.dto.DealExchangeRateDto;
import by.afinny.investments.openfeign.apigateway.ApiGatewayClient;
import by.afinny.investments.service.CalculationService;
import by.afinny.investments.service.InvestmentCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvestmentCalculationServiceImpl implements InvestmentCalculationService {

    private final ApiGatewayClient apiGatewayClient;
    private final CalculationService calculationService;

    @Override
    public List<ChangingPriceAssetDto> getExchangeRate(UUID brokerageAccountId, String authorization) {

        AvailableCurrenciesDto.AllAvailableCurrencies allAvailableCurrencies =
                apiGatewayClient.getAvailableCurrencies(authorization).getBody();

        List<DealExchangeRateDto> deals =
                calculationService.getDealsForCalculation(brokerageAccountId);

        return calculatePriceChange(deals, allAvailableCurrencies);
    }


    private List<ChangingPriceAssetDto> calculatePriceChange(List<DealExchangeRateDto> deals,
                                                              AvailableCurrenciesDto.AllAvailableCurrencies currencies) {

        List<ChangingPriceAssetDto> exchangeRateList = new ArrayList<>();

        for (DealExchangeRateDto deal : deals) {
            Optional<AvailableCurrenciesDto> moexDto = currencies.getCurrencies().stream()
                    .filter(c -> Objects.equals(c.getSecid(), deal.getSecId()) &
                            Objects.equals(c.getBoardid(), deal.getBoardId()))
                    .filter(c -> c.getLast() != 0.0)
                    .findFirst();

            moexDto.ifPresentOrElse(
                    dto -> calculate(dto.getLast(),
                                                deal.getAmount(),
                                                deal.getPurchasePrice(),
                                                deal.getSecId(),
                                                deal.getBoardId(),
                                                exchangeRateList),
                    () -> exchangeRateList.add(new ChangingPriceAssetDto(deal.getSecId(),
                                                                         deal.getBoardId(),
                                                                         "0.00",
                                                                         "0.00"))
            );
        }

        return exchangeRateList;
    }

    private void calculate(double last,
                           int amount,
                           BigDecimal purchasePrice,
                           String secId,
                           String boardId,
                           List<ChangingPriceAssetDto> exchangeRateList) {

        double tmp = last * amount;
        String changingPriceAssetRubles = BigDecimal.valueOf(tmp).subtract(
                purchasePrice.multiply(BigDecimal.valueOf(amount))
        ).toString();

        String changingPriceAssetPercent = (BigDecimal.valueOf(last).subtract(purchasePrice))
                .divide(purchasePrice, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .toString();

        ChangingPriceAssetDto dto = new ChangingPriceAssetDto(secId, boardId,
                changingPriceAssetRubles, changingPriceAssetPercent);

        exchangeRateList.add(dto);
    }

}
