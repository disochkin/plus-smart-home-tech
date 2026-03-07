package ru.yandex.practicum.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewProductDto {
    @NotNull
    @Size(min = 1, max = 512)
    private String productName;

    @NotNull
    @Size(min = 1, max = 7000)
    private String description;

    @NotNull
    @Size(min = 1, max = 512)
    private String imageSrc;

    @NotNull
    private QuantityState quantityState;

    @NotNull
    private ProductState productState;

    @NotNull
    private ProductCategory productCategory;

    @Positive
    private BigDecimal price;
}
