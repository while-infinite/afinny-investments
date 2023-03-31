package by.afinny.investments.controller;

import by.afinny.investments.dto.DealExchangeRateDto;
import by.afinny.investments.service.CalculationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculationController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CalculationService calculationService;

    private final String DEALS_FOR_CALCULATION_URL = "/auth/investment-calculations/investment";
    private static final UUID brokerageAccountId = UUID.randomUUID();
    private List<DealExchangeRateDto> dealsDtoList;

    @BeforeEach
    public void setUp() {
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
    }

    @Test
    @DisplayName("If request for getting deals then response body should be redirected")
    void getDealsForCalculation() throws Exception {
        //ARRANGE
        when(calculationService.getDealsForCalculation(any(UUID.class)))
                .thenReturn(dealsDtoList);
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get(DEALS_FOR_CALCULATION_URL)
                        .param("brokerageAccountId", brokerageAccountId.toString()))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(new ObjectMapper().writeValueAsString(dealsDtoList), mvcResult.getResponse().getContentAsString());
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}