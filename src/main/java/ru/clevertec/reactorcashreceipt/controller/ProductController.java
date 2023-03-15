package ru.clevertec.reactorcashreceipt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Product", description = "The Product API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Find all products", tags = "Product")
    @GetMapping
    public Flux<ProductDto> findAll() {
        return productService.findAll();
    }

    @Operation(
            summary = "Find product by {id}", tags = "Product",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "2")
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDto>> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Save new product", tags = "Product",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(description = "RequestBody for product",
                    content = @Content(schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject("""
                            {
                              "quantity": 1,
                              "name": "Spicy pretzel",
                              "price": "5.76",
                              "promotion": true
                            }
                            """))
            )
    )
    @PostMapping
    public Mono<ResponseEntity<ProductDto>> save(@RequestBody ProductDto productDto) {
        return productService.save(productDto)
                .map(savedProduct -> ResponseEntity.status(HttpStatus.CREATED).body(savedProduct))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update product quantity by id", tags = "Product",
            parameters = {
                    @Parameter(name = "id", description = "Enter product id here",
                            example = "3"),
                    @Parameter(name = "quantity", description = "Enter product quantity here",
                            example = "9")
            }
    )
    @PatchMapping
    public Mono<ResponseEntity<ProductDto>> update(@RequestParam Long id, Integer quantity) {
        return productService.update(id, quantity)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete product by {id}", tags = "Product",
            parameters = @Parameter(name = "id", description = "Enter product id here", example = "1")
    )
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteById(@PathVariable Long id) {
        return productService.deleteById(id)
                .then(Mono.just(ResponseEntity.ok("The product with ID " + id + " has been deleted")))
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No product with ID " + id + " to delete"));
    }

}
