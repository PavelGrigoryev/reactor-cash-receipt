package ru.clevertec.reactorcashreceipt.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.clevertec.reactorcashreceipt.dto.ProductDto;
import ru.clevertec.reactorcashreceipt.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class CashReceiptLogicServiceImpl implements CashReceiptLogicService {

    private final ProductService productService;
    private final DiscountCardService discountCardService;
    private final CashReceiptInformationService cashReceiptInformationService;
    private final UploadFileService uploadFileService;

    @Override
    public Mono<String> createCashReceipt(String idAndQuantity, String discountCardNumber) {
        StringBuilder cashReceiptHeader = cashReceiptInformationService
                .createCashReceiptHeader(LocalDate.now(), LocalTime.now());
        StringBuilder promoDiscountBuilder = new StringBuilder();
        final BigDecimal[] promoDiscount = {new BigDecimal("0")};
        return getProductDtoFlux(idAndQuantity)
                .doOnNext(productDto -> cashReceiptHeader
                        .append(cashReceiptInformationService.createCashReceiptBody(productDto)))
                .doOnNext(productDto -> promotionFilter(promoDiscountBuilder, promoDiscount, productDto))
                .map(ProductDto::total)
                .reduce(BigDecimal::add)
                .flatMap(totalSum -> getCashReceiptResults(
                        discountCardNumber,
                        cashReceiptHeader,
                        promoDiscountBuilder,
                        promoDiscount,
                        totalSum
                ))
                .map(StringBuilder::toString)
                .doOnNext(uploadFileService::uploadFile)
                .log("CashReceiptLogicServiceImpl createCashReceipt");
    }

    private Flux<ProductDto> getProductDtoFlux(String idAndQuantity) {
        return Flux.fromStream(idAndQuantity.lines())
                .flatMap(s -> Flux.fromArray(s.split(" ")))
                .map(s -> s.split("-"))
                .flatMap(strings -> productService.update(Long.valueOf(strings[0]), Integer.valueOf(strings[1]))
                        .flux());
    }

    private void promotionFilter(StringBuilder promoDiscountBuilder, BigDecimal[] promoDiscount, ProductDto productDto) {
        if (Boolean.TRUE.equals(productDto.promotion()) && productDto.quantity() > 5) {
            BigDecimal promo = getPromotionDiscount(productDto);
            promoDiscountBuilder.append(cashReceiptInformationService
                    .createCashReceiptPromoDiscount(productDto.name(), promo));
            promoDiscount[0] = promoDiscount[0].add(promo);
        }
    }

    private Mono<StringBuilder> getCashReceiptResults(String discountCardNumber,
                                                      StringBuilder cashReceiptHeader,
                                                      StringBuilder promoDiscountBuilder,
                                                      BigDecimal[] promoDiscount,
                                                      BigDecimal totalSum) {
        return discountCardService.findByDiscountCardNumber(discountCardNumber)
                .map(discountCardDto -> {
                    BigDecimal discount = getDiscount(totalSum, discountCardDto.discountPercentage());
                    return cashReceiptHeader.append(
                            cashReceiptInformationService.createCashReceiptResults(
                                    totalSum,
                                    discountCardDto.discountPercentage(),
                                    discount,
                                    promoDiscountBuilder,
                                    totalSum.subtract(discount).subtract(promoDiscount[0]))
                    );
                });
    }

    private BigDecimal getPromotionDiscount(ProductDto productDto) {
        return productDto.total()
                .multiply(BigDecimal.valueOf(0.1))
                .stripTrailingZeros();
    }

    private BigDecimal getDiscount(BigDecimal totalSum, BigDecimal percentage) {
        return totalSum.multiply(BigDecimal.valueOf(0.01)
                .multiply(percentage));
    }

}
