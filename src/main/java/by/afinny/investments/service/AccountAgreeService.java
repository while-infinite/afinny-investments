package by.afinny.investments.service;

import by.afinny.investments.entity.AccountAgree;

import java.util.UUID;

public interface AccountAgreeService {
    AccountAgree createAccountAgree();

    void updateStatus(UUID id, boolean isActive);
}
