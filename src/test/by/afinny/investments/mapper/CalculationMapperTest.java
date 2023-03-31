package by.afinny.investments.mapper;

import by.afinny.investments.dto.DealExchangeRateDto;
import by.afinny.investments.entity.Asset;
import by.afinny.investments.entity.BrokerageAccount;
import by.afinny.investments.entity.Deal;
import by.afinny.investments.entity.constant.AssetType;
import by.afinny.investments.entity.constant.DealType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CalculationMapperTest {

    @InjectMocks
    private CalculationMapperImpl calculationMapperImpl;
    @Mock
    private BrokerageAccount brokerageAccount;
    @Mock
    private Asset asset;
    private final UUID TEST_CLIENT_ID = UUID.fromString("cc6588da-ffaf-4c00-a3bd-2e0c6d83655d");

    List<Deal> deals;
    Deal deal;
    List<DealExchangeRateDto> dealExchangeRateDtoList;
    DealExchangeRateDto dealExchangeRateDto;

    @BeforeAll
    void setUp() {
        deal = Deal.builder()
                .id(TEST_CLIENT_ID)
                .brokerageAccount(brokerageAccount)
                .dealType(DealType.REFILL)
                .assetType(AssetType.CURRENCY)
                .amount(10)
                .purchasePrice(new BigDecimal(1000))
                .sum(new BigDecimal(10000))
                .dateDeal(LocalDate.now())
                .commission(new BigDecimal(2))
                .build();

        deals = List.of(
                deal,
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
    @DisplayName("Verify fields setting for List<DealExchangeRateDto>")
    void toDealExchangeRateDtoList() {
        //ACT
        dealExchangeRateDtoList = calculationMapperImpl.toDealExchangeRateDtoList(deals);
        //VERIFY
        verifyDealsDtoList(dealExchangeRateDtoList);
    }

    @Test
    @DisplayName("Verify fields setting for DealExchangeRateDto")
    void toDealExchangeRateDto() {
        //ACT
        dealExchangeRateDto = calculationMapperImpl.toDealExchangeRateDto(deal);
        //VERIFY
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(dealExchangeRateDto.getSecId()).isEqualTo(asset.getSecId());
            softAssertions.assertThat(dealExchangeRateDto.getBoardId()).isEqualTo(asset.getBoardId());
            softAssertions.assertThat(dealExchangeRateDto.getAmount()).isEqualTo(deal.getAmount());
            softAssertions.assertThat(dealExchangeRateDto.getPurchasePrice()).isEqualTo(deal.getPurchasePrice());
        });
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