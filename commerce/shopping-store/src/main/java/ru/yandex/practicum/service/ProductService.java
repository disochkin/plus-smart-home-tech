package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.ShoppingStore.ProductDto;
import ru.yandex.practicum.dto.ShoppingStore.QuantityState;
import ru.yandex.practicum.model.Product;

import java.util.UUID;

public interface ProductService {
    ProductDto getProductById(UUID productId);

    Page<Product> getProducts(String productCategory, Pageable pageable);

    ProductDto create(ProductDto productDto);

    ProductDto update(ProductDto productDto);

    void deactivateProduct(UUID uuid);

    void changeQuantityState(UUID productId, QuantityState quantityState);
}
