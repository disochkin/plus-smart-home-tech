package ru.yandex.practicum.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductShortDto {
    private UUID productId;

    private String productName;

    private String description;

    private String imageSrc;

    private QuantityState quantityState;

    private ProductState productState;

    private ProductCategory productCategory;

    private BigDecimal price;

}
