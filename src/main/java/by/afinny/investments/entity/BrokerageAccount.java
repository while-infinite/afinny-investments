package by.afinny.investments.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = BrokerageAccount.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class BrokerageAccount {

    public static final String TABLE_NAME = "brokerage_account";

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "client_id",nullable = false)
    private UUID clientId;

    @Column(name = "name_account", nullable = false)
    private String nameAccount;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "rubles", nullable = false)
    private BigDecimal rubles;

    @Column(name = "invested_money", nullable = false)
    private BigDecimal investedMoney;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @ToString.Exclude
    private AccountAgree accountAgree;

    @OneToMany(
            mappedBy = "brokerageAccount",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    private List<Deal> deals;

    public void addDeal(Deal deal) {
        deals.add(deal);
        deal.setBrokerageAccount(this);
    }

    public void removeDeal(Deal deal) {
        deals.remove(deal);
        deal.setBrokerageAccount(null);
    }
}
