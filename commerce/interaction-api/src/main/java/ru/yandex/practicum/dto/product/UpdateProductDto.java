package ru.yandex.practicum.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UpdateProductDto {
    private UUID productId;

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

    public boolean hasProductName() {
        return productName != null;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public boolean hasImageSrc() {
        return imageSrc != null;
    }

    public boolean hasQuantityState() {
        return quantityState != null;
    }

    public boolean hasProductState() {
        return productState != null;
    }

    public boolean hasProductCategory() {
        return productCategory != null;
    }

    public boolean hasPrice() {
        return price != null;
    }
}
