package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.product.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    @Mapping(target = "shoppingCartId", source = "uuid")
    @Mapping(target = "products", source = "products")
    ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart);
}