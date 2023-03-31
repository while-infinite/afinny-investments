package by.afinny.investments;

import by.afinny.investments.dto.*;
import by.afinny.investments.entity.Asset;
import by.afinny.investments.entity.Deal;
import by.afinny.investments.entity.constant.AssetType;
import by.afinny.investments.entity.constant.DealType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static by.afinny.investments.entity.constant.AssetType.CURRENCY;

public class InvestmentTestBase {

    protected final static UUID CLIENT_ID = UUID.fromString("00000000-0000-0001-0000-000000000001");
    protected final static UUID BROKERAGE_ACCOUNT_ID = UUID.fromString("00000000-0000-0002-0000-000000000002");

    protected static BrokerageAccountInfoDto brokerageAccountInfoDto;
    protected static ResponseMoneySumDto responseMoneySumDto;
    private static List<AvailableCurrenciesDto> availableCurrenciesDto;
    private static List<ChangingPriceAssetDto> pricesDtoList;
    private static List<AssetInfoDto> assetInfoDtoList;

    private static List<Deal> deals;

    protected InvestmentTestBase() {
        init();
    }

    private void init() {
        pricesDtoList = List.of(
                ChangingPriceAssetDto.builder()
                        .secId("EUR000TODTOM")
                        .boardId("AUCB")
                        .changingPriceAssetRubles("-20.00")
                        .changingPriceAssetPercent("-0.20")
                        .build(),
                ChangingPriceAssetDto.builder()
                        .secId("EURRUB_SPT")
                        .boardId("AUCB")
                        .changingPriceAssetRubles("20.00")
                        .changingPriceAssetPercent("0.20")
                        .build(),
                ChangingPriceAssetDto.builder()
                        .secId("EURRUB_TOD1D")
                        .boardId("AUCB")
                        .changingPriceAssetRubles("0.00")
                        .changingPriceAssetPercent("0.00")
                        .build()
        );

        availableCurrenciesDto = List.of(
                AvailableCurrenciesDto.builder()
                        .secid("EUR000TODTOM")
                        .boardid("AUCB")
                        .last(0.0)
                        .change(0.0)
                        .lasttoprevprice(0.0)
                        .build(),
                AvailableCurrenciesDto.builder()
                        .secid("EURRUB_SPT")
                        .boardid("AUCB")
                        .last(0.0)
                        .change(0.0)
                        .lasttoprevprice(0.0)
                        .build(),
                AvailableCurrenciesDto.builder()
                        .secid("EURRUB_TOD1D")
                        .boardid("AUCB")
                        .last(0.0)
                        .change(0.0)
                        .lasttoprevprice(0.0)
                        .build()
        );

        assetInfoDtoList = new ArrayList<>() {{
            add(new AssetInfoDto().builder()
                    .assetId(UUID.randomUUID())
                    .assetType(CURRENCY)
                    .name("testAssetName")
                    .amount(30)
                    .description("testAssetDescription")
                    .last(new BigDecimal("17.2"))
                    .changingPriceAssetRubles(new BigDecimal("2.43"))
                    .changingPriceAssetPercent(new BigDecimal("0.8"))
                    .build());
        }};

        brokerageAccountInfoDto = BrokerageAccountInfoDto.builder()
                .brokerageAccountId(BROKERAGE_ACCOUNT_ID)
                .nameAccount("testAccountName")
                .brokerageAccountQuantity(new BigDecimal("100"))
                .rubles(new BigDecimal("60"))
                .changingQuantityRubles(new BigDecimal("15"))
                .changingQuantityPercent(new BigDecimal("6"))
                .assetInfoDtoList(assetInfoDtoList)
                .build();

        responseMoneySumDto = new ResponseMoneySumDto().builder()
                .changingQuantityRubles(new BigDecimal("30"))
                .changingQuantityPercent(new BigDecimal("20"))
                .brokerageAccountQuantity(new BigDecimal("50"))
                .build();

        fillDealsList();
    }

    public static List<AvailableCurrenciesDto> getAvailableCurrenciesDto() {
        return availableCurrenciesDto;
    }

    public static List<ChangingPriceAssetDto> getPricesDtoList() {
        return pricesDtoList;
    }

    public static List<Deal> getDeals() {
        return deals;
    }

    private void fillDealsList() {
        deals = new ArrayList();

        for (int i = 0; i < getAvailableCurrenciesDto().size(); i++) {
            Asset currencyAsset = new Asset().builder()
                    .secId(getAvailableCurrenciesDto().get(i).getSecid())
                    .assetType(CURRENCY)
                    .name("name")
                    .description("description")
                    .build();

            Deal purchaseDeal = new Deal().builder()
                    .asset(currencyAsset)
                    .amount(20)
                    .dealType(DealType.PURCHASE_OF_AN_ASSET)
                    .build();

            Deal saleDeal = new Deal().builder()
                    .asset(currencyAsset)
                    .amount(10)
                    .dealType(DealType.SALE_OF_AN_ASSET)
                    .build();
            deals.add(purchaseDeal);
            deals.add(saleDeal);
        }

    }
}
