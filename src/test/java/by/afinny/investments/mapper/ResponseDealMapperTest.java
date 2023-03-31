package by.afinny.investments.mapper;

import by.afinny.investments.dto.ResponseDealDto;
import by.afinny.investments.entity.Asset;
import by.afinny.investments.entity.BrokerageAccount;
import by.afinny.investments.entity.Deal;
import by.afinny.investments.entity.constant.DealType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ExtendWith(MockitoExtension.class)
@DisplayName("Verification of correct data generation. It will pass if the fields of the entity and dto are equal")
class ResponseDealMapperTest {

    @InjectMocks
    private ResponseDealMapperImpl responseDealMapper;

    private Deal deal;
    private Asset asset;
    private BrokerageAccount brokerageAccount;

    @BeforeEach
    void setUp() {
        asset = new Asset();
        brokerageAccount = new BrokerageAccount();
        deal = Deal.builder()
                .id(UUID.randomUUID())
                .dealType(DealType.REFILL)
                .amount(100)
                .purchasePrice(BigDecimal.valueOf(1000))
                .sellingPrice(BigDecimal.valueOf(1000))
                .sum(BigDecimal.valueOf(1000))
                .dateDeal(LocalDate.now())
                .commission(BigDecimal.valueOf(1000))
                .asset(asset)
                .brokerageAccount(brokerageAccount)
                .build();

    }

    @Test
    @DisplayName("verify List<ResponseDealDto> fields settings")
    void toResponseDeal_shouldReturnListResponseDealDto() {
        List<ResponseDealDto> responseDealDto = responseDealMapper.dealToResponseDealDto(List.of(deal));
        verifyResponseDealDto(responseDealDto.get(0));
    }

    @Test
    @DisplayName("verify ResponseDealDto fields settings")
    void toResponseDeal_shouldReturnResponseDealDto() {
        ResponseDealDto responseDealDto = responseDealMapper.dealToResponseDealDto(deal);
        verifyResponseDealDto(responseDealDto);
    }

    private void verifyResponseDealDto(ResponseDealDto responseDealDto) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(responseDealDto.getDealType()).isEqualTo(deal.getDealType().toString());
            softAssertions.assertThat(responseDealDto.getSum()).isEqualTo(deal.getSum());
            softAssertions.assertThat(responseDealDto.getAssetId()).isEqualTo(deal.getAsset().getId());
            softAssertions.assertThat(responseDealDto.getPurchasePrice()).isEqualTo(deal.getPurchasePrice());
            softAssertions.assertThat(responseDealDto.getSellingPrice()).isEqualTo(deal.getSellingPrice());
            softAssertions.assertThat(responseDealDto.getDateDeal()).isEqualTo(deal.getDateDeal());
            softAssertions.assertThat(responseDealDto.getCommission()).isEqualTo(deal.getCommission());
        });
    }

}