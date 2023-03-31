package by.afinny.investments.entity;

import by.afinny.investments.entity.constant.AssetType;
import by.afinny.investments.entity.constant.DealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = Deal.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class Deal {

    public static final String TABLE_NAME = "deal";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "deal_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DealType dealType;

    @Column(name = "asset_type")
    @Enumerated(EnumType.STRING)
    private AssetType assetType;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @Column(name = "sum")
    private BigDecimal sum;

    @Column(name = "date_deal", nullable = false)
    private LocalDate dateDeal;

    @Column(name = "commission")
    private BigDecimal commission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brokerage_account_id")
    private BrokerageAccount brokerageAccount;

}
