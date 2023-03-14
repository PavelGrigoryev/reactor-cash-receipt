package ru.clevertec.reactorcashreceipt.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.clevertec.reactorcashreceipt.dto.DiscountCardDto;
import ru.clevertec.reactorcashreceipt.exception.NoSuchDiscountCardException;
import ru.clevertec.reactorcashreceipt.mapper.DiscountCardMapper;
import ru.clevertec.reactorcashreceipt.repository.DiscountCardRepository;
import ru.clevertec.reactorcashreceipt.service.DiscountCardService;

@Service
@RequiredArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final DiscountCardRepository discountCardRepository;
    private final DiscountCardMapper discountCardMapper = Mappers.getMapper(DiscountCardMapper.class);

    @Override
    public Flux<DiscountCardDto> findAll() {
        return discountCardRepository.findAll()
                .map(discountCardMapper::toDiscountCardDto)
                .log("DiscountCardServiceImpl findAll");
    }

    @Override
    public Mono<DiscountCardDto> findById(Long id) {
        return discountCardRepository.findById(id)
                .map(discountCardMapper::toDiscountCardDto)
                .switchIfEmpty(Mono.error(new NoSuchDiscountCardException("DiscountCard with ID " +
                                                                          id + " does not exist")))
                .log("DiscountCardServiceImpl findById");
    }

    @Override
    public Mono<DiscountCardDto> save(DiscountCardDto discountCardDto) {
        return Mono.just(discountCardMapper.fromDiscountCardDto(discountCardDto))
                .flatMap(discountCardRepository::save)
                .map(discountCardMapper::toDiscountCardDto)
                .log("DiscountCardServiceImpl save");
    }

    @Override
    public Mono<DiscountCardDto> findByDiscountCardNumber(String discountCardNumber) {
        return discountCardRepository.findByDiscountCardNumber(discountCardNumber)
                .map(discountCardMapper::toDiscountCardDto)
                .switchIfEmpty(Mono.error(new NoSuchDiscountCardException("DiscountCard with card number " +
                                                                          discountCardNumber + " does not exist")))
                .log("DiscountCardServiceImpl findByDiscountCardNumber");
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return discountCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new NoSuchDiscountCardException("No discount card with ID " + id + " to delete")))
                .flatMap(discountCardRepository::delete)
                .log("DiscountCardServiceImpl deleteById");
    }

}
