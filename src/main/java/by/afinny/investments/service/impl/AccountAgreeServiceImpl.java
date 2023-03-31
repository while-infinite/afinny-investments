package by.afinny.investments.service.impl;

import by.afinny.investments.entity.AccountAgree;
import by.afinny.investments.repository.AccountAgreeRepository;
import by.afinny.investments.service.AccountAgreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountAgreeServiceImpl implements AccountAgreeService {

    private final AccountAgreeRepository accountAgreeRepository;

    @Override
    @Transactional
    public AccountAgree createAccountAgree() {
        log.info("createAccountAgree() method invoke ");

        AccountAgree accountAgree = AccountAgree.builder()
                .agreeDate(LocalDate.now())
                .isActive(false)
                .build();

        accountAgree = accountAgreeRepository.save(accountAgree);
        log.debug("AccountAgree saved");

        return accountAgree;
    }

    @Override
    @Transactional
    public void updateStatus(UUID id, boolean isActive) {
        log.info("updateStatus() method invoke ");

        AccountAgree accountAgree = accountAgreeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccountAgree with id " + id + " was not found"));

        accountAgree.setIsActive(isActive);
        accountAgreeRepository.save(accountAgree);

        log.debug("AccountAgree status updated to: {}", isActive);
    }
}
