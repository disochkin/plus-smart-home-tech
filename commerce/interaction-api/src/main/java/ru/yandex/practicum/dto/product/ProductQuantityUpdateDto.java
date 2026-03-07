package ru.yandex.practicum.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductQuantityUpdateDto {
    @NotNull
    private UUID productId;

    @NotNull
    private QuantityState quantityState;
}
