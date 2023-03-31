package by.afinny.investments.dto;

import by.afinny.investments.entity.constant.AssetType;
import by.afinny.investments.entity.constant.DealType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestNewSaleDto {
    private UUID idBrokerageAccount;
    private UUID idAsset;
    private Integer amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal LAST;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal OFFER;
    private AssetType assetType;
    private DealType dealType;
    private LocalDate dateDeal;
}
