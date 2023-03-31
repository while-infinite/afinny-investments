package by.afinny.investments.repository;

import by.afinny.investments.entity.AccountAgree;
import by.afinny.investments.entity.BrokerageAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"/schema-h2.sql"}
)
@ActiveProfiles("test")
class BrokerageAccountRepositoryTest {

    @Autowired
    private BrokerageAccountRepository brokerageAccountRepository;

    private List<BrokerageAccount> brokerageAccounts;

    private final UUID clientId = UUID.fromString("00000000-0000-0001-0000-000000000001");


    @Autowired
    private AccountAgreeRepository accountAgreeRepository;

    @BeforeEach
    void lookup() {

        AccountAgree accountAgree = AccountAgree.builder()
                .agreeDate(LocalDate.now())
                .isActive(true)
                .build();


        BrokerageAccount brokerageAccount = BrokerageAccount.builder()
                .clientId(clientId)
                .nameAccount("100")
                .rubles(new BigDecimal(10))
                .investedMoney(new BigDecimal(15))
                .quantity(5)
                .accountAgree(accountAgree)
                .build();

        AccountAgree accountAgree2 = AccountAgree.builder()
                .agreeDate(LocalDate.now())
                .isActive(true)
                .build();

        BrokerageAccount brokerageAccount2 = BrokerageAccount.builder()
                .clientId(clientId)
                .nameAccount("200")
                .rubles(new BigDecimal(10))
                .investedMoney(new BigDecimal(15))
                .quantity(5)
                .accountAgree(accountAgree2)
                .build();


        accountAgreeRepository.save(accountAgree);
        accountAgreeRepository.save(accountAgree2);
        brokerageAccountRepository.save(brokerageAccount);
        brokerageAccountRepository.save(brokerageAccount2);


    }

    @Test
    @DisplayName("Check that found client's BrokerageAccounts")
    @Transactional
    void whenGetBrokerageAccounts_returnListBrokerageAccounts() {

        brokerageAccounts = brokerageAccountRepository.findByClientId(clientId);

        assertThat(brokerageAccounts).hasSize(2);
        brokerageAccounts
                .forEach(brokerageAccount -> assertThat(brokerageAccount.getClientId()).isEqualTo(clientId));

    }
}