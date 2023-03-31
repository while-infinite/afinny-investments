package by.afinny.investments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseMoneySumDto {
    private BigDecimal brokerageAccountQuantity;
    private BigDecimal changingQuantityRubles;
    private BigDecimal changingQuantityPercent;
}
