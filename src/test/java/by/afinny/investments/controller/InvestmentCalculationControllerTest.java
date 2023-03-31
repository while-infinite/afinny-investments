package by.afinny.investments.controller;

import by.afinny.investments.InvestmentTestBase;
import by.afinny.investments.service.InvestmentCalculationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvestmentCalculationController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class InvestmentCalculationControllerTest extends InvestmentTestBase {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InvestmentCalculationService investmentCalculationService;

    private final String TEST_CLIENT_ID = "cc6588da-ffaf-4c00-a3bd-2e0c6d83655d";
    private final String EXCHANGE_RATE_URL = "/auth/investment-calculations/investment/exchange-rate";
    private static final UUID brokerageAccountId = UUID.randomUUID();

    @Test
    @DisplayName("If request for getting changing prices then response body should be redirected")
    void getExchangeRate_shouldRedirectResponseBody() throws Exception {
        //ARRANGE
        when(investmentCalculationService.getExchangeRate(any(UUID.class), any(String.class)))
                .thenReturn(getPricesDtoList());
        //ACT & VERIFY
        MvcResult mvcResult = mockMvc.perform(get(EXCHANGE_RATE_URL)
                        .param("brokerageAccountId", brokerageAccountId.toString())
                        .header("authorization", "auth"))
                .andExpect(status().isOk())
                .andReturn();
        verifyBody(new ObjectMapper().writeValueAsString(getPricesDtoList()), mvcResult.getResponse().getContentAsString());
    }

    private void verifyBody(String expectedBody, String actualBody) {
        assertThat(actualBody).isEqualTo(expectedBody);
    }
}