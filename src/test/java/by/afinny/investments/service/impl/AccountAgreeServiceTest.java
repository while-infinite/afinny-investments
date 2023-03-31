package by.afinny.investments.service.impl;

import by.afinny.investments.entity.AccountAgree;
import by.afinny.investments.entity.Deal;
import by.afinny.investments.entity.constant.AssetType;
import by.afinny.investments.repository.AccountAgreeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ActiveProfiles("test")
class AccountAgreeServiceTest {
    private final String STRING_CLIENT_ID = "e6376def-e541-4f03-aa0e-b7fc6fe4e1aa";
    private final UUID CLIENT_ID = UUID.fromString(STRING_CLIENT_ID);
    @InjectMocks
    private AccountAgreeServiceImpl accountAgreeService;
    @Mock
    private AccountAgreeRepository accountAgreeRepository;
    private AccountAgree accountAgree;

    @BeforeEach
    void setUp() {
        accountAgree = AccountAgree.builder()
                .agreeDate(LocalDate.now())
                .isActive(false)
                .build();
    }

    @Test
    @DisplayName("Should create and save new AccountAgree entity")
    void testCreateAccountAgree_WhenInvoked_ShouldCreateAndSaveNewAccountAgree() {
        when(accountAgreeRepository.save(any()))
                .thenReturn(accountAgree);

        accountAgreeService.createAccountAgree();

        verify(accountAgreeRepository).save(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Should update value isActive AccountAgree entity")
    void testUpdateStatus_WhenIdIsFilled_ShouldUpdateValueIsActive(boolean isActive) {
        when(accountAgreeRepository.findById(CLIENT_ID))
                .thenReturn(Optional.of(accountAgree));
        when(accountAgreeRepository.save(accountAgree))
                .thenReturn(null);

        accountAgreeService.updateStatus(CLIENT_ID, isActive);

        verify(accountAgreeRepository).save(accountAgree);
        Assertions.assertEquals(accountAgree.getIsActive(), isActive);
    }

    @Test
    @DisplayName("if not found AccountAgree by id should throw EntityNotFoundException")
    void testUpdateStatus_WhenNotFoundEntityById_ShouldThrowEntityNotFoundException() {
        UUID client_id = UUID.randomUUID();

        when(accountAgreeRepository.findById(client_id))
                .thenReturn(Optional.empty());
        Throwable thrown = assertThrows(EntityNotFoundException.class,
                () -> accountAgreeService.updateStatus(client_id, true));

        assertNotNull(thrown.getMessage());
    }

}