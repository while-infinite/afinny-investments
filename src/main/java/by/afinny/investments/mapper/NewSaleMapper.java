package by.afinny.investments.mapper;

import by.afinny.investments.dto.RequestNewSaleDto;
import by.afinny.investments.dto.ResponseSaleDto;
import by.afinny.investments.entity.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NewSaleMapper {

    @Mapping(target = "brokerageAccount.id", source = "idBrokerageAccount")
    @Mapping(target = "asset.id", source = "idAsset")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "sellingPrice",
            expression = "java(dto.getAssetType() == by.afinny.investments.entity.constant.AssetType.STOCK ? dto.getOFFER() : dto.getLAST())")
    @Mapping(target = "assetType", source = "assetType")
    @Mapping(target = "dealType", source = "dealType")
    @Mapping(target = "dateDeal", source = "dateDeal")
    Deal requestDtoToDeal(RequestNewSaleDto dto);

    @Mapping(target = "asset_id", source = "deal.asset.id")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "selling_price", source = "sellingPrice")
    @Mapping(target = "asset_type", source = "assetType")
    @Mapping(target = "dealType", source = "dealType")
    @Mapping(target = "dateDeal", source = "dateDeal")
    ResponseSaleDto saleToResponseDto(Deal deal);
}
