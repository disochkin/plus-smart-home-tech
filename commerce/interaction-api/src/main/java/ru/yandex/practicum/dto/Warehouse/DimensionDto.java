package ru.yandex.practicum.dto.Warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DimensionDto {
    @NotNull
    @Positive
    private double width;

    @NotNull
    @Positive
    private double height;

    @NotNull
    @Positive
    private double depth;
}
