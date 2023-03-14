package ru.clevertec.reactorcashreceipt.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.clevertec.reactorcashreceipt.model.DiscountCard;

@Repository
public interface DiscountCardRepository extends ReactiveCrudRepository<DiscountCard, Long> {

    Mono<DiscountCard> findByDiscountCardNumber(String discountCardNumber);

}
