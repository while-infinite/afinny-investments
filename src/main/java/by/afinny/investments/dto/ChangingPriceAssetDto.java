package by.afinny.investments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChangingPriceAssetDto {
    String secId;
    String boardId;
    String changingPriceAssetRubles;
    String changingPriceAssetPercent;
}
