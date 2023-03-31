package by.afinny.investments.repository;

import by.afinny.investments.entity.BrokerageAccount;
import by.afinny.investments.entity.Deal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DealRepository extends JpaRepository<Deal, UUID>, JpaSpecificationExecutor<Deal> {

    @EntityGraph(attributePaths = {"asset"})
    List<Deal> findAllByBrokerageAccountId(UUID brokerageAccountId);

    default Specification<Deal> findAllDeals(BrokerageAccount brokerageAccount) {
        return (root, query, builder) -> builder.equal(root.get("brokerageAccount"), brokerageAccount);
    }
}