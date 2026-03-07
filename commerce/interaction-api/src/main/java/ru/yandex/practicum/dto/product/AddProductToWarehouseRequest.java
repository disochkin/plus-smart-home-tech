package ru.yandex.practicum.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddProductToWarehouseRequest {
    private UUID productId;
    @NotNull
    @Min(value = 1, message = "Количество должно быть больше 0")
    private Integer quantity;
}
