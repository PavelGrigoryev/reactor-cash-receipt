package ru.clevertec.reactorcashreceipt.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.clevertec.reactorcashreceipt.dto.DiscountCardDto;

public interface DiscountCardService {

    Flux<DiscountCardDto> findAll();

    Mono<DiscountCardDto> findById(Long id);

    Mono<DiscountCardDto> save(DiscountCardDto discountCardDto);

    Mono<DiscountCardDto> findByDiscountCardNumber(String discountCardNumber);

    Mono<Void> deleteById(Long id);

}
