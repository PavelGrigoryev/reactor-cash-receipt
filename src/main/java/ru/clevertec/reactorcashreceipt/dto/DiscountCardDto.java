package ru.clevertec.reactorcashreceipt.dto;

import java.math.BigDecimal;

public record DiscountCardDto(
        Long id,
        String discountCardNumber,
        BigDecimal discountPercentage
) {
}
