package by.afinny.investments.controller;

import by.afinny.investments.dto.RequestNewAccountAgreeDto;
import by.afinny.investments.dto.ResponseNewAccountAgreeDto;
import by.afinny.investments.entity.AccountAgree;
import by.afinny.investments.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class OrderControllerTest {
    @MockBean
    private OrderService orderService;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UUID CLIENT_ID = UUID.fromString("e6376def-e541-4f03-aa0e-b7fc6fe4e1aa");
    private RequestNewAccountAgreeDto requestNewAccountAgreeDto;
    private ResponseNewAccountAgreeDto responseNewAccountAgreeDto;

    private AccountAgree accountAgree;

    @BeforeEach
    void setUp() {
        accountAgree = AccountAgree.builder()
                .agreeDate(LocalDate.now())
                .id(CLIENT_ID)
                .build();

        requestNewAccountAgreeDto = RequestNewAccountAgreeDto.builder()
                .firstName("Tomas")
                .middleName("Neo")
                .lastName("Anderson")
                .phoneNumber("77777777777")
                .clientId(CLIENT_ID.toString())
                .email("white_rabbit@matrix.com")
                .passportNumber("777777")
                .build();

        responseNewAccountAgreeDto = ResponseNewAccountAgreeDto.builder()
                .agreeDate(accountAgree.getAgreeDate())
                .firstName("Tomas")
                .middleName("Neo")
                .lastName("Anderson")
                .phoneNumber("77777777777")
                .clientId(CLIENT_ID.toString())
                .email("white_rabbit@matrix.com")
                .passportNumber("777777")
                .build();
    }

    @Test
    @DisplayName("if the AccountAgree successfully created then return ok status")
    void testCreateAccountAgree_WhenInvokedWithoutBody_ShouldReturnResponseAccountAgreeDto() throws Exception {
        when(orderService.registerNewAccountAgree(requestNewAccountAgreeDto))
                .thenReturn(responseNewAccountAgreeDto);

        mockMvc.perform(post("/auth/investment-order/new-account")
                        .contentType("application/json")
                        .content(asJsonString(requestNewAccountAgreeDto)))
                .andExpect(status().isOk());
    }

    private String asJsonString(Object obj) throws JsonProcessingException {
        return objectMapper.findAndRegisterModules().enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(obj);
    }

}