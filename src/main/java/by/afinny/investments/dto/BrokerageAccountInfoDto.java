package by.afinny.investments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BrokerageAccountInfoDto {
    private UUID brokerageAccountId;
    private String nameAccount;
    private BigDecimal brokerageAccountQuantity;
    private BigDecimal changingQuantityRubles;
    private BigDecimal changingQuantityPercent;
    private BigDecimal rubles;
    private List<AssetInfoDto> assetInfoDtoList;
}