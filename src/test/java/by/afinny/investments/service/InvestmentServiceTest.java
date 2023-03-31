package by.afinny.investments.service;

import by.afinny.investments.InvestmentTestBase;
import by.afinny.investments.dto.AvailableCurrenciesDto;
import by.afinny.investments.dto.BrokerageAccountDto;
import by.afinny.investments.dto.BrokerageAccountInfoDto;
import by.afinny.investments.dto.ChangingPriceAssetDto;
import by.afinny.investments.dto.RequestNewPurchaseDto;
import by.afinny.investments.dto.ResponseDealDto;
import by.afinny.investments.dto.ResponsePurchaseDto;
import by.afinny.investments.entity.AccountAgree;
import by.afinny.investments.entity.Asset;
import by.afinny.investments.entity.BrokerageAccount;
import by.afinny.investments.entity.Deal;
import by.afinny.investments.entity.constant.DealType;
import by.afinny.investments.mapper.BrokerageAccountMapper;
import by.afinny.investments.mapper.ResponseDealMapperImpl;
import by.afinny.investments.openfeign.apigateway.ApiGatewayClient;
import by.afinny.investments.repository.BrokerageAccountRepository;
import by.afinny.investments.repository.DealRepository;
import by.afinny.investments.service.impl.InvestmentServiceImpl;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static by.afinny.investments.entity.constant.AssetType.CURRENCY;
import static by.afinny.investments.entity.constant.AssetType.STOCK;
import static by.afinny.investments.entity.constant.DealType.PURCHASE_OF_AN_ASSET;
import static by.afinny.investments.entity.constant.DealType.REFILL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
class InvestmentServiceTest extends InvestmentTestBase{
    private final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0001-0000-000000000001");
    private static List<BrokerageAccount> expectedBrokerageAccounts;
    private static List<BrokerageAccountDto> expectedBrokerageAccountsDto;
    @Mock
    private BrokerageAccountRepository brokerageAccountRepository;
    @Mock
    private DealRepository dealRepository;
    @Mock
    private BrokerageAccountMapper brokerageAccountMapper;
    @Mock
    private CalculationService calculationService;
    @Mock
    private InvestmentCalculationService investmentCalculationService;
    @Mock
    private ApiGatewayClient apiGatewayClient;
    @InjectMocks
    private InvestmentServiceImpl investmentService;

    private static RequestNewPurchaseDto requestNewPurchaseDto;
    private static Deal deal;
    private static BrokerageAccount brokerageAccount;
    private static Asset asset;
    private static ResponsePurchaseDto responsePurchaseDto;

    private Specification<Deal> specification;
    private List<ResponseDealDto> responseDealDtoList;
    private Page<Deal> operations;
    private static ResponseDealDto responseDealDto;
    @Mock
    private ResponseDealMapperImpl responseDealMapper;



    @BeforeEach
    void setUp() {

        responseDealDtoList = new ArrayList<>();
        expectedBrokerageAccountsDto = new ArrayList<>();
        expectedBrokerageAccounts = new ArrayList<>();

        brokerageAccount = BrokerageAccount.builder()
                .id(BROKERAGE_ACCOUNT_ID)
                .clientId(CLIENT_ID)
                .nameAccount("test")
                .quantity(1000)
                .rubles(BigDecimal.valueOf(1000))
                .investedMoney(BigDecimal.valueOf(1000))
                .accountAgree(new AccountAgree())
                .deals(new ArrayList<>())
                .build();

        expectedBrokerageAccounts.add(BrokerageAccount.builder()
                .id(UUID.randomUUID())
                .nameAccount("tolik")
                .build());
        expectedBrokerageAccounts.add(BrokerageAccount.builder()
                .id(UUID.randomUUID())
                .nameAccount("Ivan")
                .build());

        responseDealDto = ResponseDealDto.builder()
                .id(UUID.randomUUID())
                .assetId(UUID.randomUUID())
                .dealType(String.valueOf(DealType.REFILL))
                .purchasePrice(BigDecimal.valueOf(1000))
                .sellingPrice(BigDecimal.valueOf(1000))
                .sum(BigDecimal.valueOf(1000))
                .dateDeal(LocalDate.now())
                .commission(BigDecimal.valueOf(1000))
                .build();

        responseDealDtoList.add(responseDealDto);
        requestNewPurchaseDto = RequestNewPurchaseDto.builder()
                .idBrokerageAccount(DEFAULT_ID)
                .idAsset(DEFAULT_ID)
                .amount(5)
                .BID(new BigDecimal(50))
                .assetType(STOCK)
                .dealType(PURCHASE_OF_AN_ASSET)
                .dateDeal(LocalDate.now())
                .build();

        responsePurchaseDto = ResponsePurchaseDto.builder()
                .asset_id(DEFAULT_ID)
                .amount(5)
                .purchase_price(new BigDecimal(50))
                .asset_type(CURRENCY)
                .build();

        brokerageAccount = BrokerageAccount.builder()
                .id(DEFAULT_ID)
                .clientId(DEFAULT_ID)
                .nameAccount("100")
                .rubles(new BigDecimal(10))
                .investedMoney(new BigDecimal(15))
                .quantity(5)
                .deals(getDeals())
                .build();
        asset = Asset.builder()
                .id(DEFAULT_ID)
                .secId("555")
                .name("Tolik")
                .description("description")
                .assetType(CURRENCY)
                .boardId("555")
                .build();

        deal = Deal.builder()
                .id(DEFAULT_ID)
                .dealType(REFILL)
                .assetType(CURRENCY)
                .amount(5)
                .purchasePrice(new BigDecimal(50))
                .sellingPrice(new BigDecimal(40))
                .sum(new BigDecimal(100))
                .dateDeal(LocalDate.now())
                .commission(new BigDecimal(1))
                .asset(asset)
                .brokerageAccount(brokerageAccount)
                .build();

        specification = (root, query, builder) -> builder.equal(root.get("brokerageAccount"), brokerageAccount);
        operations = Page.empty(PageRequest.of(0, 4));
    }

    @Test
    @DisplayName("Return list of deal when program was found")
    void getDetailsDeals_shouldReturnListResponseDealDto() {
        //ARRANGE
        when(dealRepository.findAllDeals(brokerageAccount))
                .thenReturn(specification);
        when(dealRepository.findAll(specification, PageRequest.of(0, 4)))
                .thenReturn(operations);
        when(responseDealMapper.dealToResponseDealDto(operations.stream().collect(Collectors.toList())))
                .thenReturn(responseDealDtoList);
        when(brokerageAccountRepository.findById(BROKERAGE_ACCOUNT_ID)).thenReturn(Optional.of(brokerageAccount));
        //ACT
        List<ResponseDealDto> responseOperationDtoList = investmentService.getDetailsDeals(BROKERAGE_ACCOUNT_ID,CLIENT_ID,
                0, 4);
        //VERIFY
        assertThat(responseOperationDtoList).isEqualTo(responseDealDtoList);
    }

    @Test
    @DisplayName("If deal wasn't found then throw exception")
    void getDetailsDeals_ifNotSuccess_thenThrow() {
        //ARRANGE
        when(dealRepository.findAllDeals(brokerageAccount))
                .thenReturn(specification);
        when(dealRepository.findAll(specification, PageRequest.of(0, 4)))
                .thenReturn(operations);
        when(responseDealMapper.dealToResponseDealDto(operations.stream().collect(Collectors.toList())))
                .thenThrow(RuntimeException.class);
        when(brokerageAccountRepository.findById(BROKERAGE_ACCOUNT_ID)).thenReturn(Optional.of(brokerageAccount));
        //ACT
        ThrowableAssert.ThrowingCallable dealInvokeMethod = () -> investmentService.getDetailsDeals(BROKERAGE_ACCOUNT_ID, CLIENT_ID,
                0, 4);
        //VERIFY
        assertThatThrownBy(dealInvokeMethod).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("If success then actual and expected amount of brokerageAccount are equals")
    void getClientBrokerageAccounts() {


        //ARRANGE
        when(brokerageAccountRepository.findByClientId(DEFAULT_ID))
                .thenReturn(expectedBrokerageAccounts);
        when(brokerageAccountMapper.brokerageAccountsToBrokerageAccountDto(expectedBrokerageAccounts))
                .thenReturn(expectedBrokerageAccountsDto);

        //ACT
        List<BrokerageAccountDto> actualBrokerageAccount = investmentService.getClientBrokerageAccounts(DEFAULT_ID);

        //VERIFY
        verify(brokerageAccountRepository).findByClientId(DEFAULT_ID);
        assertThat(actualBrokerageAccount).isEqualTo(expectedBrokerageAccountsDto);

    }

    @Test
    @DisplayName("Get brokerage account info")
    void getClientBrokerageAccount_shouldReturnCorrectBodyData() throws Exception {

        //ARRANGE
        when(brokerageAccountRepository.findById(brokerageAccount.getId())).thenReturn(Optional.of(brokerageAccount));
        when(calculationService.getMoneySum(any(UUID.class), any(String.class))).thenReturn(responseMoneySumDto);
        when(investmentCalculationService.getExchangeRate(any(UUID.class), any(String.class))).thenReturn(getPricesDtoList());
        when(apiGatewayClient.getAvailableCurrencies(any(String.class))).thenReturn(ResponseEntity.ok(new AvailableCurrenciesDto.AllAvailableCurrencies()
                                                            .builder().currencies(getAvailableCurrenciesDto()).build()));
        //ACT
        BrokerageAccountInfoDto result = investmentService.getBrokerageAccountInfoById(brokerageAccount.getId(), "auth");
        //VERIFY
        verifyBrokerageAccountInfoDto(result);
    }

    private void verifyBrokerageAccountInfoDto(BrokerageAccountInfoDto result) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).isNotNull();
            softAssertions.assertThat(result.getBrokerageAccountId()).isEqualTo(brokerageAccount.getId());
            softAssertions.assertThat(result.getNameAccount()).isEqualTo(brokerageAccount.getNameAccount());
            softAssertions.assertThat(result.getBrokerageAccountQuantity()).isEqualTo(responseMoneySumDto.getBrokerageAccountQuantity());
            softAssertions.assertThat(result.getChangingQuantityPercent()).isEqualTo(responseMoneySumDto.getChangingQuantityPercent());
            softAssertions.assertThat(result.getChangingQuantityRubles()).isEqualTo(responseMoneySumDto.getChangingQuantityRubles());
            softAssertions.assertThat(result.getRubles()).isEqualTo(brokerageAccount.getRubles());
            result.getAssetInfoDtoList().forEach(assetInfoDto -> {
                ChangingPriceAssetDto relativeChangingPriceAssetDto = getPricesDtoList().stream()
                        .filter(changingPriceAssetDto -> changingPriceAssetDto.getSecId().equals(assetInfoDto.getSecId())).findFirst().get();
                softAssertions.assertThat(assetInfoDto.getChangingPriceAssetPercent()).isEqualTo(
                        relativeChangingPriceAssetDto.getChangingPriceAssetPercent());
                softAssertions.assertThat(assetInfoDto.getChangingPriceAssetRubles()).isEqualTo(
                        relativeChangingPriceAssetDto.getChangingPriceAssetRubles());
                softAssertions.assertThat(assetInfoDto.getAmount()).isEqualTo(10);
            });
        });
    }
}