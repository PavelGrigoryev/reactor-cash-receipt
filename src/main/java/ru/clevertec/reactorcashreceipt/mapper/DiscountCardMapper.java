package ru.clevertec.reactorcashreceipt.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.reactorcashreceipt.dto.DiscountCardDto;
import ru.clevertec.reactorcashreceipt.model.DiscountCard;

@Mapper
public interface DiscountCardMapper {

    DiscountCardDto toDiscountCardDto(DiscountCard discountCard);

    DiscountCard fromDiscountCardDto(DiscountCardDto discountCardDto);

}
