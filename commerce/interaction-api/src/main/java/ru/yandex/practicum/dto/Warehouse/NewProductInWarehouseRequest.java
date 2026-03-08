package ru.yandex.practicum.dto.Warehouse;

import lombok.Data;

import java.util.UUID;

@Data
public class NewProductInWarehouseRequest {
    private UUID productId;
    private Boolean fragile;
    private double weight;
    private DimensionDto dimension;
}
