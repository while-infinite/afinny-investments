package by.afinny.investments.dto;

import by.afinny.investments.entity.constant.AssetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetInfoDto {

    private UUID assetId;
    private int amount;
    private BigDecimal last;
    private BigDecimal changingPriceAssetRubles;
    private BigDecimal changingPriceAssetPercent;
    private AssetType assetType;
    private String secId;
    private String name;
    private String description;
}
