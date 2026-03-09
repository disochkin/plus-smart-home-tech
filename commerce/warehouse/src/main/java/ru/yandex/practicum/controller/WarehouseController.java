package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.Warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.Warehouse.AddressDto;
import ru.yandex.practicum.dto.Warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.Warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.ProductWarehouse;
import ru.yandex.practicum.service.WarehouseProductService;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@Validated
public class WarehouseController {
    final WarehouseProductService warehouseProductService;

    @PutMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductWarehouse create(@Valid @RequestBody NewProductInWarehouseRequest newProductInWarehouseRequest) {
        log.debug("Запрос на создание нового продукта на складе: {}", newProductInWarehouseRequest);
        return warehouseProductService.create(newProductInWarehouseRequest);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void add(@Valid @RequestBody AddProductToWarehouseRequest addProductToWarehouseRequest) {
        log.debug("Запрос на добавление нового продукта на складе: {}", addProductToWarehouseRequest);
        warehouseProductService.add(addProductToWarehouseRequest);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto check(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        log.debug("Проверка наличия на складе: {}", shoppingCartDto);
        return warehouseProductService.check(shoppingCartDto);
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto address() {
        log.debug("Запрос адреса");
        return warehouseProductService.address();
    }
}
