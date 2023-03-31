package by.afinny.investments.mapper;

import by.afinny.investments.dto.BrokerageAccountDto;
import by.afinny.investments.entity.BrokerageAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper

public interface BrokerageAccountMapper {

    List<BrokerageAccountDto> brokerageAccountsToBrokerageAccountDto(List<BrokerageAccount> credits);

    @Mapping(target = "brokerageAccountId", source = "id")
    @Mapping(target = "nameAccount", source = "nameAccount")
    @Mapping(target = "brokerageAccountQuantity", source = "quantity")
    BrokerageAccountDto brokerageAccountToBrokerageAccountDto(BrokerageAccount brokerageAccount);

}


