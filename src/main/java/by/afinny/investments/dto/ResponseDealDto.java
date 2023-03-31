package by.afinny.investments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseDealDto implements Comparable<ResponseDealDto> {

    private UUID id;
    private UUID assetId;
    private String dealType;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private BigDecimal sum;
    private LocalDate dateDeal;
    private BigDecimal commission;


    @Override
    public int compareTo(ResponseDealDto responseDealDto) {
        return responseDealDto.getDateDeal().compareTo(this.getDateDeal());
    }
}
