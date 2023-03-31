package by.afinny.investments.service.impl;

import by.afinny.investments.dto.DealExchangeRateDto;
import by.afinny.investments.dto.ResponseMoneySumDto;
import by.afinny.investments.entity.BrokerageAccount;
import by.afinny.investments.entity.Deal;
import by.afinny.investments.mapper.CalculationMapper;
import by.afinny.investments.repository.BrokerageAccountRepository;
import by.afinny.investments.repository.DealRepository;
import by.afinny.investments.service.ApiGatewayService;
import by.afinny.investments.service.CalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculationServiceImpl implements CalculationService {

    private final DealRepository dealRepository;
    private final CalculationMapper calculationMapper;
    private final BrokerageAccountRepository brokerageAccountRepository;
    private final ApiGatewayService apiGatewayService;

    @Override
    public List<DealExchangeRateDto> getDealsForCalculation(UUID brokerageAccountId) {
        log.info("getDealsForCalculation() method invoked");
        return calculationMapper
                .toDealExchangeRateDtoList(dealRepository.findAllByBrokerageAccountId(brokerageAccountId));
    }

    @Override
    public ResponseMoneySumDto getMoneySum(UUID brokerageAccountId, String authorization) {
        log.info("getMoneySum() invoked");
        Map<String, Double> secIdsAndLasts = new HashMap<>();
        apiGatewayService.getAvailableStocks(authorization)
                .getAvailableStocksDtoList()
                .forEach(obj -> secIdsAndLasts.put(obj.getSecId(), obj.getLast() == null ? 0 : obj.getLast().doubleValue()));
        apiGatewayService.getAvailableCurrencies(authorization)
                .getCurrencies()
                .forEach(obj -> secIdsAndLasts.put(obj.getSecid(), obj.getLast()));
        BrokerageAccount brokerageAccount = brokerageAccountRepository
                .findById(brokerageAccountId)
                .orElseThrow(() -> new EntityNotFoundException("BrokerageAccount with id=" + brokerageAccountId + " didn't find"));
        List<Deal> deals = brokerageAccount.getDeals();
        double amountsSum = deals.stream().filter(deal -> deal.getAmount() != null).mapToDouble(deal -> deal.getAmount() * secIdsAndLasts.get(deal.getAsset().getSecId())).sum();
        BigDecimal brokerageAccountQuantity = new BigDecimal(amountsSum).setScale(2, RoundingMode.FLOOR).add(brokerageAccount.getRubles());
        BigDecimal investedMoney = brokerageAccount.getInvestedMoney();
        BigDecimal changingQuantityRubles = brokerageAccountQuantity.subtract(investedMoney);
        BigDecimal changingQuantityPercent = changingQuantityRubles.multiply(new BigDecimal(100)).divide(investedMoney, 2, RoundingMode.FLOOR);
        return ResponseMoneySumDto.builder()
                .brokerageAccountQuantity(brokerageAccountQuantity)
                .changingQuantityRubles(changingQuantityRubles)
                .changingQuantityPercent(changingQuantityPercent)
                .build();
    }
}
