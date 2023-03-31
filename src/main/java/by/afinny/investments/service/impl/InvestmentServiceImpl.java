package by.afinny.investments.service.impl;

import by.afinny.investments.dto.*;
import by.afinny.investments.entity.Asset;
import by.afinny.investments.entity.BrokerageAccount;
import by.afinny.investments.entity.Deal;
import by.afinny.investments.mapper.BrokerageAccountMapper;
import by.afinny.investments.mapper.NewPurchaseMapper;
import by.afinny.investments.mapper.NewSaleMapper;
import by.afinny.investments.mapper.ResponseDealMapper;
import by.afinny.investments.openfeign.apigateway.ApiGatewayClient;
import by.afinny.investments.repository.AssetRepository;
import by.afinny.investments.repository.BrokerageAccountRepository;
import by.afinny.investments.repository.DealRepository;
import by.afinny.investments.service.ApiGatewayService;
import by.afinny.investments.service.CalculationService;
import by.afinny.investments.service.InvestmentCalculationService;
import by.afinny.investments.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestmentServiceImpl implements InvestmentService {
    private final BrokerageAccountRepository brokerageAccountRepository;
    private final DealRepository dealRepository;
    private final AssetRepository assetRepository;
    private final BrokerageAccountMapper brokerageAccountMapper;
    private final ResponseDealMapper responseDealMapper;
    private final NewPurchaseMapper newPurchaseMapper;
    private final NewSaleMapper newSaleMapper;
    private final ApiGatewayClient apiGatewayClient;
    private final CalculationService calculationService;
    private final ApiGatewayService apiGatewayService;
    private final InvestmentCalculationService investmentCalculationService;


    @Override
    public List<BrokerageAccountDto> getClientBrokerageAccounts(UUID clientId) {
        log.info("getClientBrokerageAccounts() invoked");

        List<BrokerageAccount> clientBrokerageAccounts =
                brokerageAccountRepository.findByClientId(clientId);
        return brokerageAccountMapper.brokerageAccountsToBrokerageAccountDto(clientBrokerageAccounts);
    }

    @Override
    public BrokerageAccountInfoDto getBrokerageAccountInfoById(UUID brokerageAccountId, String authorization) {
        log.info("getClientBrokerageAccount() invoked");

        BrokerageAccount clientBrokerageAccount =
                brokerageAccountRepository.findById(brokerageAccountId)
                        .orElseThrow(() -> new EntityNotFoundException("Brokerage account with id " + brokerageAccountId
                                + " can't be found"));

        List<AssetInfoDto> assetInfoDtoList = new ArrayList<>();

        Map<AvailableCurrenciesDto, List<Deal>> currencyDealMap = clientBrokerageAccount.getDeals()
                .stream().collect(Collectors.groupingBy(deal -> {
            return apiGatewayClient.getAvailableCurrencies(authorization).getBody()
                    .getCurrencies().stream().filter(apiCurrency -> apiCurrency.getSecid().equals(deal.getAsset().getSecId())).findFirst()
                    .orElseThrow(() -> new RuntimeException("Can't find currency with id" + deal.getAsset().getSecId()));
        }));


        fillAssetList(assetInfoDtoList, currencyDealMap, clientBrokerageAccount.getId(), authorization);

        ResponseMoneySumDto brokerageAccountMoneyInfo = calculationService.getMoneySum(clientBrokerageAccount.getId(), authorization);

        return new BrokerageAccountInfoDto().builder()
                .brokerageAccountId(clientBrokerageAccount.getId())
                .nameAccount(clientBrokerageAccount.getNameAccount())
                .brokerageAccountQuantity(brokerageAccountMoneyInfo.getBrokerageAccountQuantity())
                .changingQuantityRubles(brokerageAccountMoneyInfo.getChangingQuantityRubles())
                .changingQuantityPercent(brokerageAccountMoneyInfo.getChangingQuantityPercent())
                .rubles(clientBrokerageAccount.getRubles())
                .assetInfoDtoList(assetInfoDtoList)
                .build();
    }

    @Override
    public List<ResponseDealDto> getDetailsDeals(UUID brokerageAccountId, UUID clientId, Integer
            pageNumber, Integer pageSize) {
        log.info("getDetailsDeals() method invoke");
        if (pageSize < 1) {
            pageSize = 4;
        }
        BrokerageAccount brokerageAccount = brokerageAccountRepository.findById(brokerageAccountId).orElseThrow(
                () -> new EntityNotFoundException("brokerage account with id " + brokerageAccountId + " wasn't found"));
        Specification<Deal> spec = dealRepository.findAllDeals(brokerageAccount);
        Page<Deal> dealPage = dealRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));
        return responseDealMapper.dealToResponseDealDto(dealPage.stream().sorted().collect(Collectors.toList()));
    }

    @Override
    public String getBrokerageAccountName(UUID brokerageAccountId) {
        BrokerageAccount brokerageAccount = brokerageAccountRepository
                .findById(brokerageAccountId)
                .orElseThrow(() -> new EntityNotFoundException("BrokerageAccount with id=" + brokerageAccountId + " didn't find"));
        return brokerageAccount.getNameAccount();
    }


    private Asset getAsset(UUID assetId) {
        return assetRepository.findById(assetId).orElseThrow(
                () -> new EntityNotFoundException("asset with id " + assetId + " wasn't found"));
    }

    private BrokerageAccount getBrokerageAccount(UUID brokerageAccountId) {
        return brokerageAccountRepository.findById(brokerageAccountId).orElseThrow(
                () -> new EntityNotFoundException("brokerageAccount with id " + brokerageAccountId + " wasn't found"));
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponsePurchaseDto createPurchase(RequestNewPurchaseDto request, String authorization) {
        log.info("createPurchase() invoked");

        if (!checkNeededStock(request.getIdAsset(), authorization).isEmpty()) {
            request.setBID(new BigDecimal(checkNeededStock(request.getIdAsset(), authorization)
                    .get(0).getBId()));
        } else {
            request.setLAST(BigDecimal.valueOf(checkNeededCurrencies(request.getIdAsset(), authorization)
                    .get(0).getLast()).setScale(2, RoundingMode.HALF_UP));
        }

        Deal deal = newPurchaseMapper.requestDtoToDeal(request);
        deal.setAsset(getAsset(request.getIdAsset()));
        deal.setBrokerageAccount(getBrokerageAccount(request.getIdBrokerageAccount()));

        Deal savedDeal = dealRepository.save(deal);

        return newPurchaseMapper.purchaseToResponseDto(savedDeal);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseSaleDto createSale(RequestNewSaleDto request, String authorization) {
        log.info("createSale() invoked");

        if (!checkNeededStock(request.getIdAsset(), authorization).isEmpty()) {
            request.setOFFER(checkNeededStock(request.getIdAsset(), authorization)
                    .get(0).getOffer().setScale(2, RoundingMode.HALF_UP));
        } else {
            request.setLAST(new BigDecimal(checkNeededCurrencies(request.getIdAsset(), authorization)
                    .get(0).getLast()).setScale(2, RoundingMode.HALF_UP));
        }

        Deal deal = newSaleMapper.requestDtoToDeal(request);
        deal.setAsset(getAsset(request.getIdAsset()));
        deal.setBrokerageAccount(getBrokerageAccount(request.getIdBrokerageAccount()));

        Deal savedDeal = dealRepository.save(deal);

        return newSaleMapper.saleToResponseDto(savedDeal);
    }

    public List<AvailableStocksDto> checkAvailableStocks(String authorization) {
        List<AvailableStocksDto> listAllAvailableStocks = apiGatewayService.getAvailableStocks(authorization)
                .getAvailableStocksDtoList();
        return listAllAvailableStocks;
    }

    public List<AvailableCurrenciesDto> checkAvailableCurrencies(String authorization) {
        List<AvailableCurrenciesDto> listAllAvailableCurrencies = apiGatewayService.getAvailableCurrencies(authorization)
                .getCurrencies();
        return listAllAvailableCurrencies;
    }

    public List<AvailableStocksDto> checkNeededStock(UUID assetId, String authorization) {
        List<AvailableStocksDto> listNeededStocks = checkAvailableStocks(authorization).stream()
                .filter(x -> x.getSecId().equals(getAsset(assetId).getSecId()))
                .collect(Collectors.toList());
        return listNeededStocks;
    }

    public List<AvailableCurrenciesDto> checkNeededCurrencies(UUID assetId, String authorization) {
        List<AvailableCurrenciesDto> listNeededCurrencies = checkAvailableCurrencies(authorization).stream()
                .filter(y -> y.getSecid().equals(getAsset(assetId).getSecId()))
                .collect(Collectors.toList());
        return listNeededCurrencies;
    }

    private void fillAssetList(List<AssetInfoDto> targetList, Map<AvailableCurrenciesDto, List<Deal>> currencyDealMap, UUID
            brokerageAccountId, String authorization) {

        currencyDealMap.forEach((currency, dealCollection) -> {
            Asset asset = dealCollection.stream().findFirst().get().getAsset();
            Integer amount = calculateAmountOfAsset(dealCollection);
            ChangingPriceAssetDto changingPriceAsset = investmentCalculationService.getExchangeRate(brokerageAccountId, authorization).stream()
                    .filter(changingPriceAssetDto -> changingPriceAssetDto.getSecId().equals(asset.getSecId())).findFirst().get();

            targetList.add(new AssetInfoDto().builder()
                    .secId(asset.getSecId())
                    .amount(amount)
                    .last(BigDecimal.valueOf(currency.getLast()))
                    .changingPriceAssetRubles(new BigDecimal(changingPriceAsset.getChangingPriceAssetRubles()))
                    .changingPriceAssetPercent(new BigDecimal(changingPriceAsset.getChangingPriceAssetPercent()))
                    .assetType(asset.getAssetType())
                    .name(asset.getName())
                    .description(asset.getDescription())
                    .build());
        });
    }

    private Integer calculateAmountOfAsset(Collection<Deal> targetList) {
        AtomicInteger result = new AtomicInteger(0);

        targetList.forEach(deal -> {
            switch (deal.getDealType()) {
                case PURCHASE_OF_AN_ASSET:
                    result.addAndGet(deal.getAmount());
                    break;
                case SALE_OF_AN_ASSET:
                    result.set(result.get() - deal.getAmount());
                    break;
            }
        });
        return result.get();
    }
}
