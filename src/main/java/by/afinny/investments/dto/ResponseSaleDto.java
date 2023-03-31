package by.afinny.investments.dto;

import by.afinny.investments.entity.constant.AssetType;
import by.afinny.investments.entity.constant.DealType;
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
public class ResponseSaleDto {
    private UUID asset_id;
    private Integer amount;
    private BigDecimal selling_price;
    private AssetType asset_type;
    private DealType dealType;
    private LocalDate dateDeal;
}
