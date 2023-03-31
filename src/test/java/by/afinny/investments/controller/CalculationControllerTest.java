package by.afinny.investments.controller;

import by.afinny.investments.dto.ResponseMoneySumDto;
import by.afinny.investments.service.CalculationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculationController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CalculationService calculationService;

    private ResponseMoneySumDto responseMoneySumDto;

    private final UUID randomUUID = UUID.randomUUID();
    private final String authorizationHeader = "authorization";

    @BeforeAll
    public void setUp() {
        responseMoneySumDto = ResponseMoneySumDto.builder()
                .brokerageAccountQuantity(new BigDecimal("66729"))
                .changingQuantityRubles(new BigDecimal("6729"))
                .changingQuantityPercent(new BigDecimal("11.21"))
                .build();
    }

    @Test
    @DisplayName("If getMoneySum was successfully then return ResponseMoneySumDto")
    public void getMoneySum_shouldReturnResponseMoneySumDto() throws Exception {
        //ARRANGE
        when(calculationService.getMoneySum(randomUUID, authorizationHeader)).thenReturn(responseMoneySumDto);

        //ACT
        MvcResult result = mockMvc.perform(get("/auth/investment-calculations/investment-sum")
                        .param("brokerageAccountId", randomUUID.toString())
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isOk())
                .andReturn();

        //VERIFY
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(responseMoneySumDto));
    }

    @Test
    @DisplayName("If getMoneySum wasn't successfully then return BAD REQUEST")
    public void getMoneySum_shouldReturnBadRequest() throws Exception {
        //ARRANGE
        when(calculationService.getMoneySum(randomUUID, authorizationHeader)).thenThrow(new EntityNotFoundException("test message"));

        //ACT & VERIFY
        mockMvc.perform(get("/auth/investment-calculations/investment-sum")
                        .param("brokerageAccountId", randomUUID.toString())
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader))
                .andExpect(status().isBadRequest());
    }

}