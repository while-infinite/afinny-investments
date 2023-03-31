package by.afinny.investments.service;

import by.afinny.investments.dto.AvailableCurrenciesDto;
import by.afinny.investments.dto.AvailableStocksDto;
import by.afinny.investments.dto.ResponseMoneySumDto;
import by.afinny.investments.entity.Asset;
import by.afinny.investments.entity.BrokerageAccount;
import by.afinny.investments.entity.Deal;
import by.afinny.investments.entity.constant.AssetType;
import by.afinny.investments.openfeign.apigateway.ApiGatewayClient;
import by.afinny.investments.repository.BrokerageAccountRepository;
import by.afinny.investments.service.impl.CalculationServiceImpl;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
public class CalculationServiceTest {

    @InjectMocks
    private CalculationServiceImpl calculationService;

    @Mock
    private BrokerageAccountRepository brokerageAccountRepository;
    @Mock
    private ApiGatewayService apiGatewayService;

    private AvailableStocksDto.AllAvailableStocks allAvailableStocks;
    private AvailableCurrenciesDto.AllAvailableCurrencies allAvailableCurrencies;
    private BrokerageAccount brokerageAccount;
    private ResponseMoneySumDto responseMoneySumDto;
    private final UUID brokerageAccountId = UUID.randomUUID();
    private final String token = "token";

    @BeforeEach
    public void setUp() {
        allAvailableStocks = AvailableStocksDto.AllAvailableStocks.builder()
                .availableStocksDtoList(Collections.singletonList(AvailableStocksDto.builder()
                        .secId("STOCK")
                        .last(new BigDecimal("150.5"))
                        .build()))
                .build();

        allAvailableCurrencies = AvailableCurrenciesDto.AllAvailableCurrencies.builder()
                .currencies(Collections.singletonList(AvailableCurrenciesDto.builder()
                        .secid("CURRENCY")
                        .last(50.25)
                        .build()))
                .build();

        Asset stockAsset = Asset.builder()
                .secId("STOCK")
                .assetType(AssetType.STOCK)
                .build();
        Deal stockDeal = Deal.builder()
                .asset(stockAsset)
                .amount(2)
                .build();
        Asset currencyAsset = Asset.builder()
                .secId("CURRENCY")
                .assetType(AssetType.CURRENCY)
                .build();
        Deal currencyDeal = Deal.builder()
                .asset(currencyAsset)
                .amount(4)
                .build();

        brokerageAccount = BrokerageAccount.builder()
                .deals(Arrays.asList(currencyDeal, stockDeal))
                .id(brokerageAccountId)
                .investedMoney(new BigDecimal("150.00"))
                .rubles(new BigDecimal("1000.00"))
                .build();

        responseMoneySumDto = ResponseMoneySumDto.builder()
                .brokerageAccountQuantity(new BigDecimal("1502.00"))
                .changingQuantityRubles(new BigDecimal("1352.00"))
                .changingQuantityPercent(new BigDecimal("901.33"))
                .build();
    }

    @Test
    @DisplayName("If getMoneySum was successful then return ResponseMoneySumDto")
    void getMoneySum_shouldReturnResponseMoneySumDto() {
        //ARRANGE
        when(apiGatewayService.getAvailableStocks(token)).thenReturn(allAvailableStocks);
        when(apiGatewayService.getAvailableCurrencies(token)).thenReturn(allAvailableCurrencies);
        when(brokerageAccountRepository.findById(brokerageAccountId)).thenReturn(Optional.of(brokerageAccount));
        //ACT
        ResponseMoneySumDto moneySumDto = calculationService.getMoneySum(brokerageAccountId, token);
        //VERIFY
        assertThat(moneySumDto).usingRecursiveComparison().isEqualTo(responseMoneySumDto);
    }

    @Test()
    @DisplayName("If getMoneySum wasn't successful then throw exception")
    void getMoneySum_shouldThrow() {
        //ARRANGE
        when(apiGatewayService.getAvailableStocks(token)).thenReturn(allAvailableStocks);
        when(apiGatewayService.getAvailableCurrencies(token)).thenReturn(allAvailableCurrencies);
        when(brokerageAccountRepository.findById(brokerageAccountId)).thenReturn(Optional.empty());
        //ACT
        ThrowableAssert.ThrowingCallable thrown = () -> calculationService.getMoneySum(brokerageAccountId, token);
        //VERIFY
        assertThatThrownBy(thrown).isInstanceOf(EntityNotFoundException.class);
    }

}