package ru.clevertec.reactorcashreceipt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("product")
public class Product {

    @Id
    private Long id;

    private Integer quantity;

    private String name;

    private BigDecimal price;

    private BigDecimal total;

    private Boolean promotion;

}
