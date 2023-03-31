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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = AccountAgree.TABLE_NAME)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class AccountAgree {

    public static final String TABLE_NAME = "account_agree";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "agree_date", nullable = false)
    private LocalDate agreeDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToOne(mappedBy = "accountAgree",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private BrokerageAccount brokerageAccount;
}
