package ru.clevertec.reactorcashreceipt.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.clevertec.reactorcashreceipt.dto.ProductDto;

public interface ProductService {

    Flux<ProductDto> findAll();

    Mono<ProductDto> findById(Long id);

    Mono<ProductDto> save(ProductDto productDto);

    Mono<ProductDto> update(Long id, Integer quantity);

    Mono<Void> deleteById(Long id);

}
