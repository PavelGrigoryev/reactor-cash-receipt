package ru.clevertec.reactorcashreceipt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("discount_card")
public class DiscountCard {

    @Id
    private Long id;

    @Column("discount_card_number")
    private String discountCardNumber;

    @Column("discount_percentage")
    private BigDecimal discountPercentage;

}
