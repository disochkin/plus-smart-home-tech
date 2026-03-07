package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.product.NewProductDto;
import ru.yandex.practicum.dto.product.ProductShortDto;
import ru.yandex.practicum.dto.product.QuantityState;
import ru.yandex.practicum.dto.product.UpdateProductDto;
import ru.yandex.practicum.model.Product;

import java.util.UUID;

public interface ProductService {
    ProductShortDto getProductById(UUID productId);

    Page<Product> getProducts(String productCategory, Pageable pageable);

    ProductShortDto create(NewProductDto newProductDto);

    ProductShortDto update(UpdateProductDto updateProductDto);

    void deactivateProduct(UUID uuid);

    void changeQuantityState(UUID productId, QuantityState quantityState);
}
