package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.ShoppingStore.ProductDto;
import ru.yandex.practicum.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toShortDto(Product product);

    Product toProduct(ProductDto productDto);

}





