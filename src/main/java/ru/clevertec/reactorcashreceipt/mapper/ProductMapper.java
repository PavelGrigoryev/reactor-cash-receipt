package ru.clevertec.reactorcashreceipt.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.reactorcashreceipt.dto.ProductDto;
import ru.clevertec.reactorcashreceipt.model.Product;

@Mapper
public interface ProductMapper {

    ProductDto toProductDto(Product product);

    Product fromProductDto(ProductDto productDto);

}
