package by.afinny.investments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AvailableStocksDto implements Serializable {
    @JsonProperty("SECID")
    private String secId;
    @JsonProperty("BOARDID")
    private String boardId;
    @JsonProperty("BID")
    private Integer bId;
    @JsonProperty("OFFER")
    private BigDecimal offer;
    @JsonProperty("LAST")
    private BigDecimal last;
    @JsonProperty("CHANGE")
    private Integer change;
    @JsonProperty("LASTTOPREVPRICE")
    private BigDecimal lastTopRevPrice;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class AllAvailableStocks implements Serializable {
        List<AvailableStocksDto> availableStocksDtoList;
    }
}
