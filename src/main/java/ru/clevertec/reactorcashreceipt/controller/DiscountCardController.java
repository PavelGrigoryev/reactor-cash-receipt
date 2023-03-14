package ru.clevertec.reactorcashreceipt.controller;

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
public class DiscountCardController {

    private final DiscountCardService discountCardService;

    @GetMapping
    public Flux<DiscountCardDto> findAll() {
        return discountCardService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DiscountCardDto>> findById(@PathVariable Long id) {
        return discountCardService.findById(id)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<DiscountCardDto>> save(@RequestBody DiscountCardDto discountCardDto) {
        return discountCardService.save(discountCardDto)
                .map(savedDiscountCardDto -> ResponseEntity.status(HttpStatus.CREATED).body(savedDiscountCardDto))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteById(@PathVariable Long id) {
        return discountCardService.deleteById(id)
                .then(Mono.just(ResponseEntity.ok("The discount card with ID " + id + " has been deleted")))
                .onErrorReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No discount card wit ID " + id + " to delete"));
    }

}
