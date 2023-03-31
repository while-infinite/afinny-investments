package by.afinny.investments.mapper;

import by.afinny.investments.dto.RequestNewPurchaseDto;
import by.afinny.investments.dto.ResponsePurchaseDto;
import by.afinny.investments.entity.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NewPurchaseMapper {

    @Mapping(target = "brokerageAccount.id", source = "idBrokerageAccount")
    @Mapping(target = "asset.id", source = "idAsset")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "purchasePrice",
            expression = "java(dto.getAssetType() == by.afinny.investments.entity.constant.AssetType.STOCK ? dto.getBID() : dto.getLAST())")
    @Mapping(target = "assetType", source = "assetType")
    @Mapping(target = "dealType", source = "dealType")
    @Mapping(target = "dateDeal", source = "dateDeal")
    Deal requestDtoToDeal(RequestNewPurchaseDto dto);

    @Mapping(target = "asset_id", source = "deal.asset.id")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "purchase_price", source = "purchasePrice")
    @Mapping(target = "asset_type", source = "assetType")
    @Mapping(target = "dealType", source = "dealType")
    @Mapping(target = "dateDeal", source = "dateDeal")
    ResponsePurchaseDto purchaseToResponseDto(Deal deal);


}
