package by.afinny.investments.controller;

import by.afinny.investments.dto.RequestNewAccountAgreeDto;
import by.afinny.investments.dto.ResponseNewAccountAgreeDto;
import by.afinny.investments.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("auth/investment-order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("new-account")
    public ResponseEntity<ResponseNewAccountAgreeDto> createAccountAgree(@RequestBody RequestNewAccountAgreeDto requestNewAccountAgreeDto) {
        ResponseNewAccountAgreeDto responseNewAccountAgreeDto = orderService.registerNewAccountAgree(requestNewAccountAgreeDto);
        return ResponseEntity.ok(responseNewAccountAgreeDto);
    }
}
