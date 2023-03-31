package by.afinny.investments.controller;

import by.afinny.investments.dto.BrokerageAccountDto;
import by.afinny.investments.dto.ResponseDealDto;
import by.afinny.investments.dto.RequestNewPurchaseDto;
import by.afinny.investments.dto.RequestNewSaleDto;
import by.afinny.investments.dto.ResponsePurchaseDto;
import by.afinny.investments.dto.ResponseSaleDto;
import by.afinny.investments.dto.BrokerageAccountInfoDto;
import by.afinny.investments.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth/investment")
public class InvestmentController {
    public static final String URL_BROKER_ACCOUNT = "/auth/investment";
    public static final String PARAM_CLIENT_ID = "clientId";
    public static final String URL_HISTORY = "/history";
    public static final String BROKERAGE_ACCOUNT_ID = "/{brokerageAccountId}";

    private final InvestmentService investmentService;

    @GetMapping
    public ResponseEntity<List<BrokerageAccountDto>> getClientBrokerageAccounts(@RequestParam UUID clientId) {
        List<BrokerageAccountDto> clientCurrentBrokerageAccounts = investmentService.getClientBrokerageAccounts(clientId);

        return ResponseEntity.ok(clientCurrentBrokerageAccounts);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ResponseDealDto>> getDetailsDeals(@RequestParam UUID brokerageAccountId,
                                                                 @RequestParam UUID clientId,
                                                                 @RequestParam Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize) {
        List<ResponseDealDto> responseDealDto = investmentService.getDetailsDeals(brokerageAccountId, clientId, pageNumber, pageSize);
        return ResponseEntity.ok(responseDealDto);
    }
    @PostMapping("/new-purchase")
    public ResponseEntity<ResponsePurchaseDto> createNewPurchase(@RequestBody RequestNewPurchaseDto requestNewPurchaseDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization ) {
        return ResponseEntity.ok(investmentService.createPurchase(requestNewPurchaseDto, authorization));
    }
    @PostMapping("/new-sale")
    public ResponseEntity<ResponseSaleDto> createNewSale(@RequestBody RequestNewSaleDto requestNewSaleDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization ) {
        return ResponseEntity.ok(investmentService.createSale(requestNewSaleDto, authorization));
    }

    @GetMapping("/brokerageAccountInfo/{brokerageAccountId}")
    public ResponseEntity<BrokerageAccountInfoDto> getClientBrokerageAccount(@PathVariable UUID brokerageAccountId,
                                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        return ResponseEntity.ok(investmentService.getBrokerageAccountInfoById(brokerageAccountId, authorization));
    }

    @GetMapping("/brokerage")
    public ResponseEntity<String> getBrokerageAccountName(@RequestParam UUID brokerageAccountId){
        String brokerageAccountName = investmentService.getBrokerageAccountName(brokerageAccountId);
        return ResponseEntity.ok(brokerageAccountName);
    }
}
