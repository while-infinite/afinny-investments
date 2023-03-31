package by.afinny.investments.repository;

import by.afinny.investments.entity.BrokerageAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface BrokerageAccountRepository extends JpaRepository<BrokerageAccount, UUID> {

    List<BrokerageAccount> findByClientId(UUID clientId);
}

