package ru.yandex.practicum.dto.product;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDeleteRequest {
    private String productId;

    public UUID getProductId() {
        return productId != null ? UUID.fromString(productId) : null;
    }
}