package by.afinny.investments.service.impl;

import by.afinny.investments.dto.DealExchangeRateDto;
import by.afinny.investments.entity.Asset;
import by.afinny.investments.entity.BrokerageAccount;
import by.afinny.investments.entity.Deal;
import by.afinny.investments.entity.constant.AssetType;
import by.afinny.investments.entity.constant.DealType;
import by.afinny.investments.repository.DealRepository;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
class CalculationServiceImplTest {

    @InjectMocks
    private CalculationServiceImpl calculationServiceImpl;
    @Mock
    private DealRepository dealRepository;
    @Mock
    private BrokerageAccount brokerageAccount;
    @Mock
    private Asset asset;
    private final UUID TEST_CLIENT_ID = UUID.fromString("cc6588da-ffaf-4c00-a3bd-2e0c6d83655d");
    private final UUID BROKERAGE_ACCOUNT_ID = UUID.fromString("cc6588da-ffaf-4c00-a3bd-2e0c6d83655d");
    private List<Deal> deals;

    @BeforeEach
    public void setUp() {
        deals = List.of(
                Deal.builder()
                        .id(TEST_CLIENT_ID)
                        .brokerageAccount(brokerageAccount)
                        .dealType(DealType.REFILL)
                        .assetType(AssetType.CURRENCY)
                        .amount(10)
                        .purchasePrice(new BigDecimal(1000))
                        .sum(new BigDecimal(10000))
                        .dateDeal(LocalDate.now())
                        .commission(new BigDecimal(2))
                        .build(),
                Deal.builder()
                        .id(TEST_CLIENT_ID)
                        .brokerageAccount(brokerageAccount)
                        .dealType(DealType.REFILL)
                        .assetType(AssetType.CURRENCY)
                        .amount(10)
                        .purchasePrice(new BigDecimal(1000))
                        .sum(new BigDecimal(10000))
                        .dateDeal(LocalDate.now())
                        .commission(new BigDecimal(2))
                        .build()
                );
    }

    @Test
    @DisplayName("If list od deals found by brokerage account ID, then return List<DealExchangeRateDto>")
    void getDealsForCalculation() {
        //ARRANGE
        when(dealRepository.findAllByBrokerageAccountId(any(UUID.class)))
                .thenReturn(deals);
        //ACT
        List<DealExchangeRateDto> dealsDtoList = calculationServiceImpl.getDealsForCalculation(BROKERAGE_ACCOUNT_ID);
        //VERIFY
        verifyDealsDtoList(dealsDtoList);
    }

    @Test
    @DisplayName("If list od deals was not found, then throw EntityNotFoundException")
    public void getClientByPhone_ifClientNotExists_thenThrowEntityNotFoundException() {
        //ARRANGE
        when(dealRepository.findAllByBrokerageAccountId(any(UUID.class))).thenThrow(EntityNotFoundException.class);
        //ACT
        ThrowableAssert.ThrowingCallable findDeals =
                () -> calculationServiceImpl.getDealsForCalculation(BROKERAGE_ACCOUNT_ID);
        //VERIFY
        Assertions.assertThatThrownBy(findDeals).isInstanceOf(EntityNotFoundException.class);
    }

    private void verifyDealsDtoList(List<DealExchangeRateDto> dealsDtoList) {
        SoftAssertions softAssertions = new SoftAssertions();
        int i = 0;
        for (DealExchangeRateDto dealDto: dealsDtoList) {
            softAssertions.assertThat(dealDto.getSecId()).isEqualTo(asset.getSecId());
            softAssertions.assertThat(dealDto.getBoardId()).isEqualTo(asset.getBoardId());
            softAssertions.assertThat(dealDto.getAmount()).isEqualTo(deals.get(i).getAmount());
            softAssertions.assertThat(dealDto.getPurchasePrice()).isEqualTo(deals.get(i).getPurchasePrice());

            i++;
            softAssertions.assertAll();
        }
    }
}