package ru.clevertec.reactorcashreceipt.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.clevertec.reactorcashreceipt.dto.ProductDto;
import ru.clevertec.reactorcashreceipt.exception.NoSuchProductException;
import ru.clevertec.reactorcashreceipt.mapper.ProductMapper;
import ru.clevertec.reactorcashreceipt.model.Product;
import ru.clevertec.reactorcashreceipt.repository.ProductRepository;
import ru.clevertec.reactorcashreceipt.service.ProductService;

import java.math.BigDecimal;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Override
    public Flux<ProductDto> findAll() {
        return productRepository.findAll()
                .map(productMapper::toProductDto)
                .sort(Comparator.comparing(ProductDto::id))
                .log("ProductServiceImpl findAll");
    }

    @Override
    public Mono<ProductDto> findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductDto)
                .switchIfEmpty(Mono.error(new NoSuchProductException("Product with ID " + id + " does not exist")))
                .log("ProductServiceImpl findById");
    }

    @Override
    public Mono<ProductDto> save(ProductDto productDto) {
        return Mono.just(productMapper.fromProductDto(productDto))
                .map(this::createProduct)
                .flatMap(productRepository::save)
                .map(productMapper::toProductDto)
                .log("ProductServiceImpl save");
    }

    @Override
    public Mono<ProductDto> update(Long id, Integer quantity) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchProductException("Product with ID " + id + " does not exist")))
                .flatMap(product -> {
                    if (!quantity.equals(product.getQuantity())) {
                        product.setQuantity(quantity);
                        product.setTotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

                        return productRepository.save(product)
                                .map(productMapper::toProductDto);
                    } else {
                        ProductDto productDto = productMapper.toProductDto(product);
                        return Mono.just(productDto);
                    }
                })
                .log("ProductServiceImpl update");
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchProductException("No product with ID " + id + " to delete")))
                .flatMap(productRepository::delete)
                .log("ProductServiceImpl deleteById");
    }

    private Product createProduct(Product product) {
        return Product.builder()
                .id(product.getId())
                .quantity(product.getQuantity())
                .name(product.getName())
                .price(product.getPrice())
                .total(product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .promotion(product.getPromotion())
                .build();
    }

}
