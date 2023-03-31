package by.afinny.investments.repository;

import by.afinny.investments.entity.AccountAgree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountAgreeRepository extends JpaRepository<AccountAgree, UUID> {
}
