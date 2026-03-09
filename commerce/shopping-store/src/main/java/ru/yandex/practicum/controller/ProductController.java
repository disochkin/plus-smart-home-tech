package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ShoppingStore.ProductChangeAnswer;
import ru.yandex.practicum.dto.ShoppingStore.ProductDto;
import ru.yandex.practicum.dto.ShoppingStore.QuantityState;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.service.ProductService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable("productId") UUID productId) {
        log.info("Запрос продукта, id={}", productId);
        return productService.getProductById(productId);
    }

    @GetMapping("")
    public Page<Product> getAllProducts(
            @RequestParam("category") String productCategory,
            Pageable pageable
    ) {
        log.info("Запрос продукта. Категория: {}, параметры пагинации: {}", productCategory, pageable);
        return productService.getProducts(productCategory, pageable);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Valid @RequestBody ProductDto productDto) {
        log.info("Запрос на добавление нового продукта: {}", productDto);
        return productService.create(productDto);
    }

    @PostMapping("")
    public ProductDto update(@RequestBody @Valid ProductDto productDto) {
        log.info("Запрос на обновление продукта: {}", productDto);
        return productService.update(productDto);
    }


    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public ProductChangeAnswer deactivateProduct(@RequestBody String uuidString) {
        UUID uuid = UUID.fromString(uuidString.trim().replaceAll("[\"']", ""));
        log.info("Запрос на удаление, id='{}'", uuid);
        productService.deactivateProduct(uuid);
        return new ProductChangeAnswer();
    }

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public ProductChangeAnswer changeQuantityState(@RequestParam("productId") UUID productId,
                                                   @RequestParam("quantityState") QuantityState quantityState) {
        log.info("Запрос на изменение количества, id='{}', ", productId);
        productService.changeQuantityState(productId, quantityState);
        return new ProductChangeAnswer();
    }
}