package by.afinny.investments.mapper;

import by.afinny.investments.dto.ResponseDealDto;
import by.afinny.investments.entity.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ResponseDealMapper {

    @Mapping(source = "deals.asset.id", target = "assetId")
    List<ResponseDealDto> dealToResponseDealDto(List<Deal> deals);

    @Mapping(source = "deal.asset.id", target = "assetId")
    ResponseDealDto dealToResponseDealDto(Deal deal);
}
