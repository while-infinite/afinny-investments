package by.afinny.investments.service;

import by.afinny.investments.dto.BrokerageAccountDto;
import by.afinny.investments.dto.BrokerageAccountInfoDto;
import by.afinny.investments.dto.ResponseDealDto;
import by.afinny.investments.dto.RequestNewPurchaseDto;
import by.afinny.investments.dto.RequestNewSaleDto;
import by.afinny.investments.dto.ResponsePurchaseDto;
import by.afinny.investments.dto.ResponseSaleDto;

import java.util.List;
import java.util.UUID;

public interface InvestmentService {
    List<BrokerageAccountDto> getClientBrokerageAccounts(UUID clientId);

    List<ResponseDealDto> getDetailsDeals(UUID brokerageAccountId, UUID clientId, Integer pageNumber, Integer pageSize);

    String getBrokerageAccountName(UUID brokerageAccountId);

    BrokerageAccountInfoDto getBrokerageAccountInfoById(UUID brokerageAccountId, String authorization);

    ResponsePurchaseDto createPurchase(RequestNewPurchaseDto requestNewPurchaseDto, String authorization);

    ResponseSaleDto createSale(RequestNewSaleDto requestNewSaleDto, String authorization);



}
