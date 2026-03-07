package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.product.DimensionDto;
import ru.yandex.practicum.dto.product.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.ProductWarehouse;
import ru.yandex.practicum.model.ProductWarehouseDimension;

@Mapper(componentModel = "spring")
public interface ProductWarehouseMapper {
    @Mapping(target = "quantity", constant = "0")
    ProductWarehouse toProductWarehouse(NewProductInWarehouseRequest newProductInWarehouseRequest);

    ProductWarehouseDimension toProductWarehouse(DimensionDto dimensionDto);
}