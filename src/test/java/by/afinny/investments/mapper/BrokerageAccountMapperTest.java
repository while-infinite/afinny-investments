package by.afinny.investments.mapper;

import by.afinny.investments.dto.BrokerageAccountDto;
import by.afinny.investments.entity.BrokerageAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Verification of correct data generation. It will pass if the fields of the entity and dto are equal")
class BrokerageAccountMapperTest {
    private final UUID CLIENT_ID = UUID.fromString("00000000-0000-0001-0000-000000000001");
    private static BrokerageAccount brokerageAccount;
    @InjectMocks
    private BrokerageAccountMapperImpl brokerageAccountMapper;

    @BeforeEach
    void setUp() {


        brokerageAccount = BrokerageAccount.builder()
                .id(UUID.randomUUID())
                .clientId(CLIENT_ID)
                .nameAccount("tolik")
                .quantity(10)
                .investedMoney(new BigDecimal(10))
                .build();
    }

    @Test
    @DisplayName("Verification of correct data generation")
    void brokerageAccountToBrokerageAccountDto_checkCorrectMappingData() throws JsonProcessingException {


        BrokerageAccountDto brokerageAccountDto = brokerageAccountMapper.brokerageAccountToBrokerageAccountDto(brokerageAccount);

        verifyBrokerageAccountDto(brokerageAccountDto, brokerageAccount);

    }

    private void verifyBrokerageAccountDto(BrokerageAccountDto brokerageAccountDto, BrokerageAccount brokerageAccount) {
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(brokerageAccountDto.getBrokerageAccountId())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(brokerageAccount.getId());
            softAssertions.assertThat(brokerageAccountDto.getNameAccount())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(brokerageAccount.getNameAccount());
            softAssertions.assertThat(brokerageAccountDto.getBrokerageAccountQuantity())
                    .withFailMessage("Id should be equals")
                    .isEqualTo(brokerageAccount.getQuantity());
        });
    }
}