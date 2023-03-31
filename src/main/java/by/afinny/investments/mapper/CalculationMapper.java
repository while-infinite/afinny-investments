package by.afinny.investments.mapper;

import by.afinny.investments.dto.DealExchangeRateDto;
import by.afinny.investments.entity.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface CalculationMapper {

    List<DealExchangeRateDto> toDealExchangeRateDtoList(List<Deal> deals);

    @Mapping(target="secId", source="asset.secId")
    @Mapping(target="boardId", source="asset.boardId")
    DealExchangeRateDto toDealExchangeRateDto(Deal deal);
}