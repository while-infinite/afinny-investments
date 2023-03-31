package by.afinny.investments.mapper;

import by.afinny.investments.dto.RequestNewPurchaseDto;
import by.afinny.investments.dto.ResponsePurchaseDto;
import by.afinny.investments.entity.Asset;
import by.afinny.investments.entity.Deal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static by.afinny.investments.entity.constant.AssetType.CURRENCY;
import static by.afinny.investments.entity.constant.DealType.REFILL;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Verification of correct data generation. It will pass if the fields of the entity and dto are equal")
class NewPurchaseMapperTest {
    private final UUID CLIENT_ID = UUID.fromString("00000000-0000-0001-0000-000000000001");
    private final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0001-0000-000000000001");
    private static Deal deal;
    @InjectMocks
    private NewPurchaseMapperImpl newPurchaseMapper;
    private RequestNewPurchaseDto requestNewPurchaseDto;

    @BeforeEach
    void setUp() {


        deal = Deal.builder()
                .dealType(REFILL)
                .assetType(CURRENCY)
                .amount(5)
                .purchasePrice(new BigDecimal(50))
                .sellingPrice(new BigDecimal(40))
                .sum(new BigDecimal(100))
                .dateDeal(LocalDate.now())
                .commission(new BigDecimal(1))
                .asset(Asset.builder()
                        .id(DEFAULT_ID)
                        .secId("555")
                        .name("Tolik")
                        .description("description")
                        .assetType(CURRENCY)
                        .boardId("555")
                        .build())
                .build();

        requestNewPurchaseDto = RequestNewPurchaseDto.builder()
                .idBrokerageAccount(DEFAULT_ID)
                .idAsset(DEFAULT_ID)
                .amount(6)
                .LAST(new BigDecimal(7))
                .assetType(CURRENCY)
                .build();

    }


    @Test
    @DisplayName("Verification of correct data generation")
    void dealToResponseDto_checkCorrectMappingData() {

        ResponsePurchaseDto responsePurchaseDto = newPurchaseMapper.purchaseToResponseDto(deal);

        verifyDealResponse(deal, responsePurchaseDto);

    }


    @Test
    @DisplayName("Verification of correct data generation")
    void requestDtoToDeal_checkCorrectMappingData() {
        deal = newPurchaseMapper.requestDtoToDeal(requestNewPurchaseDto);

        verifyDealRequest(deal, requestNewPurchaseDto);

    }


    private void verifyDealResponse(Deal deal, ResponsePurchaseDto responsePurchaseDto) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(responsePurchaseDto.getPurchase_price())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(deal.getPurchasePrice());
            softAssertions.assertThat(responsePurchaseDto.getAmount())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(deal.getAmount());
            softAssertions.assertThat(responsePurchaseDto.getAsset_id())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(deal.getAsset().getId());
            softAssertions.assertThat(responsePurchaseDto.getAsset_type())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(deal.getAssetType());
        });
    }

    private void verifyDealRequest(Deal deal, RequestNewPurchaseDto requestNewPurchaseDto) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(requestNewPurchaseDto.getIdBrokerageAccount())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(deal.getBrokerageAccount().getId());
            softAssertions.assertThat(requestNewPurchaseDto.getIdAsset())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(deal.getAsset().getId());
            softAssertions.assertThat(requestNewPurchaseDto.getAmount())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(deal.getAmount());
            softAssertions.assertThat(requestNewPurchaseDto.getAssetType())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(deal.getAssetType());
        });
    }


}