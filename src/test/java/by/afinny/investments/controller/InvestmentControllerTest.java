package by.afinny.investments.controller;

import by.afinny.investments.InvestmentTestBase;
import by.afinny.investments.dto.BrokerageAccountDto;
import by.afinny.investments.dto.BrokerageAccountInfoDto;
import by.afinny.investments.dto.RequestNewPurchaseDto;
import by.afinny.investments.dto.ResponseDealDto;
import by.afinny.investments.dto.ResponsePurchaseDto;
import by.afinny.investments.entity.constant.DealType;
import by.afinny.investments.service.InvestmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static by.afinny.investments.entity.constant.AssetType.CURRENCY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvestmentController.class)
class InvestmentControllerTest extends InvestmentTestBase {

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InvestmentService investmentService;

    private static BrokerageAccountDto brokerageAccountDto;
    private static ResponseDealDto responseDealDto;
    private static List<ResponseDealDto> responseDealDtoList;


    private static ResponsePurchaseDto responsePurchaseDto;
    private static RequestNewPurchaseDto requestNewPurchaseDto;


    @BeforeAll
    static void setUp() {

        responseDealDtoList = new ArrayList<>();

        brokerageAccountDto = BrokerageAccountDto.builder()
                .brokerageAccountId(UUID.fromString("00000000-0000-0001-0000-000000000001"))
                .nameAccount("tolik")
                .brokerageAccountQuantity(5)
                .build();

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
    }

    @Test
    @DisplayName("if the list of deal was successfully received then return status OK")
    void getDetailsDeals_shouldReturnResponseDeal() throws Exception {
        //ARRANGE
        when(investmentService.getDetailsDeals(BROKERAGE_ACCOUNT_ID, CLIENT_ID, 0, 4)).thenReturn(responseDealDtoList);
        //ACT
        MvcResult mvcResult = mockMvc.perform(get(
                        InvestmentController.URL_BROKER_ACCOUNT + InvestmentController.URL_HISTORY)
                        .param("brokerageAccountId", String.valueOf(BROKERAGE_ACCOUNT_ID))
                        .param("clientId", String.valueOf(CLIENT_ID))
                        .param("pageNumber", String.valueOf(0))
                        .param("pageSize", String.valueOf(4))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(responseDealDtoList)))
                .andExpect(status().isOk())
                .andReturn();
        //VERIFY
        verifyBody(asJsonString(responseDealDtoList), mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("if the list of deal wasn't successfully received then return Internal Server Error")
    void getDetailsDeals_ifNotSuccess_thenStatus500() throws Exception {
        //ARRANGE
        when(investmentService.getDetailsDeals(BROKERAGE_ACCOUNT_ID, CLIENT_ID, 0, 4)).thenThrow(new RuntimeException());
        //ACT & VERIFY
        mockMvc.perform(get(
                        InvestmentController.URL_BROKER_ACCOUNT + InvestmentController.URL_HISTORY)
                        .param("brokerageAccountId", String.valueOf(BROKERAGE_ACCOUNT_ID))
                        .param("clientId", String.valueOf(CLIENT_ID))
                        .param("pageNumber", String.valueOf(0))
                        .param("pageSize", String.valueOf(4))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(responseDealDtoList)))
                .andExpect(status().isInternalServerError());

        requestNewPurchaseDto = RequestNewPurchaseDto.builder()
                .idBrokerageAccount(UUID.fromString("00000000-0000-0001-0000-000000000001"))
                .idAsset(UUID.fromString("00000000-0000-0001-0000-000000000002"))
                .amount(5)
                .BID(new BigDecimal(6))
                .LAST(new BigDecimal(7))
                .assetType(CURRENCY)
                .build();

        responsePurchaseDto = ResponsePurchaseDto.builder()
                .asset_id(UUID.fromString("00000000-0000-0001-0000-000000000002"))
                .amount(5)
                .purchase_price(new BigDecimal(6))
                .asset_type(CURRENCY)
                .build();


    }

    @Test
    @DisplayName("If the list of current brokerageAccounts was successfully received then return status OK")
    void getClientBrokerageAccounts_ifSuccess_thenStatus200() throws Exception {

        //ARRANGE
        when(investmentService.getClientBrokerageAccounts(CLIENT_ID)).thenReturn(new ArrayList<>());


        //ACT
        ResultActions perform = mockMvc.perform(
                get(InvestmentController.URL_BROKER_ACCOUNT)
                        .param(InvestmentController.PARAM_CLIENT_ID, CLIENT_ID.toString()));

        //VERIFY
        perform.andExpect(status().isOk());
        MvcResult result = perform.andReturn();

        verifyClientIdRequestParameter(result);
        assertThatResponseBodyIsEmptyArray(result);
    }

    @Test
    @DisplayName("If current brokerageAccount was successfully received then return status OK")
    void getClientBrokerageAccount_ifSuccess_thenStatus200() throws Exception {

        //ARRANGE
        when(investmentService.getBrokerageAccountInfoById(BROKERAGE_ACCOUNT_ID, "auth")).thenReturn(new BrokerageAccountInfoDto());

        //ACT
        ResultActions perform = mockMvc.perform(
                get(InvestmentController.URL_BROKER_ACCOUNT + "/brokerageAccountInfo"
                        + InvestmentController.BROKERAGE_ACCOUNT_ID, BROKERAGE_ACCOUNT_ID)
                        .header("Authorization", "auth"));

        //VERIFY
        perform.andExpect(status().isOk());
    }

    @Test
    @DisplayName("If current brokerageAccount was successfully received then return correct body data")
    void getClientBrokerageAccount_shouldReturnCorrectBodyData() throws Exception {

        //ARRANGE
        when(investmentService.getBrokerageAccountInfoById(BROKERAGE_ACCOUNT_ID, "auth")).thenReturn(brokerageAccountInfoDto);

        //ACT
        ResultActions perform = mockMvc.perform(
                get(InvestmentController.URL_BROKER_ACCOUNT + "/brokerageAccountInfo"
                        + InvestmentController.BROKERAGE_ACCOUNT_ID, BROKERAGE_ACCOUNT_ID)
                        .header("Authorization", "auth"));

        //VERIFY
        MvcResult result = perform.andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(
                new ObjectMapper().writer().writeValueAsString(brokerageAccountInfoDto));
    }


    private void verifyClientIdRequestParameter(MvcResult result) {
        assertThat(result.getRequest().getParameter("clientId")).isEqualTo(CLIENT_ID.toString());
    }

    private static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat(FORMAT_DATE))
                .registerModule(new JavaTimeModule())
                .writeValueAsString(obj);
    }

    private void assertThatResponseBodyIsEmptyArray(MvcResult result) throws UnsupportedEncodingException {
        assertThat(result.getResponse().getContentAsString()).isEqualTo("[]");
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }

}