package ru.clevertec.reactorcashreceipt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.clevertec.reactorcashreceipt.dto.ProductDto;
import ru.clevertec.reactorcashreceipt.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Flux<ProductDto> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDto>> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<ProductDto>> save(@RequestBody ProductDto productDto) {
        return productService.save(productDto)
                .map(savedProduct -> ResponseEntity.status(HttpStatus.CREATED).body(savedProduct))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PatchMapping
    public Mono<ResponseEntity<ProductDto>> update(@RequestParam Long id, Integer quantity) {
        return productService.update(id, quantity)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteById(@PathVariable Long id) {
        return productService.deleteById(id)
                .then(Mono.just(ResponseEntity.ok("The product with ID " + id + " has been deleted")))
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No product with ID " + id + " to delete"));
    }

}
