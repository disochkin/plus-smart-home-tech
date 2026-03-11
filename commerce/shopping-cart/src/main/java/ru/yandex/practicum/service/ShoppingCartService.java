package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.ShoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String username);
    ShoppingCart addProductsToShoppingCartByUserName(String username, Map<UUID, Integer> products);
    void deactivateShoppingCart(String username);
    ShoppingCart remove(String username, List<UUID> products);
    ShoppingCart changeQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest);
}

