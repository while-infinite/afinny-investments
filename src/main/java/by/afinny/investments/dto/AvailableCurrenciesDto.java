package by.afinny.investments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AvailableCurrenciesDto implements Serializable {
    @JsonProperty("SECID")
    private String secid;
    @JsonProperty("BOARDID")
    private String boardid;
    @JsonProperty("LAST")
    private double last;
    @JsonProperty("CHANGE")
    private double change;
    @JsonProperty("LASTTOPREVPRICE")
    private double lasttoprevprice;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class AllAvailableCurrencies implements Serializable{
        @JsonProperty("marketdata")
        private List<AvailableCurrenciesDto> currencies;
    }
}
