package ru.clevertec.reactorcashreceipt.service;

import reactor.core.publisher.Mono;

public interface CashReceiptLogicService {

    Mono<String> createCashReceipt(String idAndQuantity, String discountCardNumber);

}
