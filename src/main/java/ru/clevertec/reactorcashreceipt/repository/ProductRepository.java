package ru.clevertec.reactorcashreceipt.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.reactorcashreceipt.model.Product;


@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

}
