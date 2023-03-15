package ru.clevertec.reactorcashreceipt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.clevertec.reactorcashreceipt.service.CashReceiptLogicService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cashReceipts")
public class CashReceiptController {

    private final CashReceiptLogicService cashReceiptLogicService;

    @GetMapping
    public Mono<ResponseEntity<String>> createCashReceipt(@RequestParam String idAndQuantity, String discountCardNumber) {
        return cashReceiptLogicService.createCashReceipt(idAndQuantity, discountCardNumber)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

}
