package by.afinny.investments.service.impl;

import by.afinny.investments.dto.AvailableCurrenciesDto;
import by.afinny.investments.dto.ChangingPriceAssetDto;
import by.afinny.investments.dto.DealExchangeRateDto;
import by.afinny.investments.openfeign.apigateway.ApiGatewayClient;
import org.assertj.core.api.SoftAssertions;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
class InvestmentCalculationServiceImplTest {
    @InjectMocks
    private InvestmentCalculationServiceImpl investmentCalculationServiceImpl;
    @Mock
    private ApiGatewayClient apiGatewayClient;
    @Mock
    private CalculationServiceImpl calculationService;

    private static final UUID brokerageAccountId = UUID.randomUUID();
    private List<DealExchangeRateDto> dealsDtoList;
    private AvailableCurrenciesDto.AllAvailableCurrencies allAvailableCurrenciesDtoList;
    List<ChangingPriceAssetDto> exchangeRateList;

    @BeforeEach
    void setUp() {
        dealsDtoList =  List.of(
                DealExchangeRateDto.builder()
                        .secId("EUR000TODTOM")
                        .boardId("AUCB")
                        .amount(10)
                        .purchasePrice(BigDecimal.valueOf(2.00))
                        .build(),
                DealExchangeRateDto.builder()
                        .secId("EURRUB_SPT")
                        .boardId("AUCB")
                        .amount(10)
                        .purchasePrice(BigDecimal.valueOf(2.00))
                        .build(),
                DealExchangeRateDto.builder()
                        .secId("EURRUB_TOD1D")
                        .boardId("AUCB")
                        .amount(10)
                        .purchasePrice(BigDecimal.valueOf(2.00))
                        .build()
        );

        List<AvailableCurrenciesDto> currencies =  List.of(
                AvailableCurrenciesDto.builder()
                        .secid("EUR000TODTOM")
                        .boardid("AUCB")
                        .last(-0.50)
                        .change(0.00)
                        .lasttoprevprice(0.00)
                        .build(),
                AvailableCurrenciesDto.builder()
                        .secid("EURRUB_SPT")
                        .boardid("AUCB")
                        .last(0.50)
                        .change(0.00)
                        .lasttoprevprice(0.00)
                        .build(),
                AvailableCurrenciesDto.builder()
                        .secid("EURRUB_TOD1D")
                        .boardid("AUCB")
                        .last(0.00)
                        .change(0.00)
                        .lasttoprevprice(0.00)
                        .build()
        );

        allAvailableCurrenciesDtoList = new AvailableCurrenciesDto.AllAvailableCurrencies();
        allAvailableCurrenciesDtoList.setCurrencies(currencies);

        exchangeRateList =  List.of(
                ChangingPriceAssetDto.builder()
                        .secId("EUR000TODTOM")
                        .boardId("AUCB")
                        .changingPriceAssetRubles(String.valueOf(-25.00))
                        .changingPriceAssetPercent("-125.00")
                        .build(),
                ChangingPriceAssetDto.builder()
                        .secId("EURRUB_SPT")
                        .boardId("AUCB")
                        .changingPriceAssetRubles(String.valueOf(-15.00))
                        .changingPriceAssetPercent("-75.00")
                        .build(),
                ChangingPriceAssetDto.builder()
                        .secId("EURRUB_TOD1D")
                        .boardId("AUCB")
                        .changingPriceAssetRubles("0.00")
                        .changingPriceAssetPercent("0.00")
                        .build()
        );
    }

    @Test
    @DisplayName("If calculation data is successfully received return the prices")
    void getExchangeRate_shouldReturnTokens() {
        //ARRANGE
        ResponseEntity<List<DealExchangeRateDto>> deals =
                ResponseEntity.ok(dealsDtoList);

        when(apiGatewayClient.getAvailableCurrencies(any(String.class)))
                .thenReturn(ResponseEntity.ok(allAvailableCurrenciesDtoList));
        when(calculationService.getDealsForCalculation(brokerageAccountId))
                .thenReturn(deals.getBody());
        //ACT
        List<ChangingPriceAssetDto> responseDto =
                investmentCalculationServiceImpl.getExchangeRate(brokerageAccountId, "auth");
        //VERIFY
        verifyPrices(responseDto);
    }

    @Test
    @DisplayName("If data from MOEX or calculation service is null then throw Runtime Exception")
    void getExchangeRate_ifImportDataEqualsNull_thenThrowException() {
        //ARRANGE
        when(apiGatewayClient.getAvailableCurrencies(any(String.class)))
                .thenReturn(null);
//        when(calculationService.getDealsForCalculation(brokerageAccountId))
//                .thenReturn(null);
        //ACT
        ThrowableAssert.ThrowingCallable exception =
                () -> investmentCalculationServiceImpl.getExchangeRate(brokerageAccountId, "auth");
        //VERIFY
        assertThatThrownBy(exception).isInstanceOf(RuntimeException.class);
    }

    private void verifyPrices(List<ChangingPriceAssetDto> responseDtoList) {
        SoftAssertions softAssertions = new SoftAssertions();
        int i = 0;
        for (ChangingPriceAssetDto responseDto: responseDtoList) {
            softAssertions.assertThat(responseDto.getSecId()).isEqualTo(exchangeRateList.get(i).getSecId());
            softAssertions.assertThat(responseDto.getBoardId()).isEqualTo(exchangeRateList.get(i).getBoardId());
            softAssertions.assertThat(responseDto.getChangingPriceAssetRubles())
                    .isEqualTo(exchangeRateList.get(i).getChangingPriceAssetRubles());
            softAssertions.assertThat(responseDto.getChangingPriceAssetPercent())
                    .isEqualTo(exchangeRateList.get(i).getChangingPriceAssetPercent());

            i++;
            softAssertions.assertAll();
        }
    }
}