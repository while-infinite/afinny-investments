package by.afinny.investments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DealExchangeRateDto {
    private String secId;
    private String boardId;
    private Integer amount;
    private BigDecimal purchasePrice;
}