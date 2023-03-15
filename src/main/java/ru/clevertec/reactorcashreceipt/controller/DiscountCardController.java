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
import ru.clevertec.reactorcashreceipt.dto.DiscountCardDto;
import ru.clevertec.reactorcashreceipt.service.DiscountCardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discountCards")
@Tag(name = "DiscountCard", description = "The DiscountCard API")
public class DiscountCardController {

    private final DiscountCardService discountCardService;

    @Operation(summary = "Find all discount cards", tags = "DiscountCard")
    @GetMapping
    public Flux<DiscountCardDto> findAll() {
        return discountCardService.findAll();
    }

    @Operation(
            summary = "Find discount card by {id}", tags = "DiscountCard",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "3")
    )
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DiscountCardDto>> findById(@PathVariable Long id) {
        return discountCardService.findById(id)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Save new discount card", tags = "DiscountCard",
            requestBody = @io.swagger.v3.oas.annotations.parameters
                    .RequestBody(description = "RequestBody for discount card",
                    content = @Content(schema = @Schema(implementation = DiscountCardDto.class),
                            examples = @ExampleObject("""
                            {
                              "discountCardNumber": "7878",
                              "discountPercentage": 7.5
                            }
                            """))
            )
    )
    @PostMapping
    public Mono<ResponseEntity<DiscountCardDto>> save(@RequestBody DiscountCardDto discountCardDto) {
        return discountCardService.save(discountCardDto)
                .map(savedDiscountCardDto -> ResponseEntity.status(HttpStatus.CREATED).body(savedDiscountCardDto))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete discount card by {id}", tags = "DiscountCard",
            parameters = @Parameter(name = "id", description = "Enter discount card id here", example = "1")
    )
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteById(@PathVariable Long id) {
        return discountCardService.deleteById(id)
                .then(Mono.just(ResponseEntity.ok("The discount card with ID " + id + " has been deleted")))
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No discount card wit ID " + id + " to delete"));
    }

}
