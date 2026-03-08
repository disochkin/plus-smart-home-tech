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
//@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@Validated
public class WarehouseController {
    final WarehouseProductService warehouseProductService;

    @PutMapping("/api/v1/warehouse")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductWarehouse create(@Valid @RequestBody NewProductInWarehouseRequest newProductInWarehouseRequest) {
        log.debug("New product warehouse create request: {}", newProductInWarehouseRequest);
        return warehouseProductService.create(newProductInWarehouseRequest);
    }

    @PostMapping("/api/v1/warehouse/add")
    @ResponseStatus(HttpStatus.OK)
    public void add(@Valid @RequestBody AddProductToWarehouseRequest addProductToWarehouseRequest) {
        log.debug("Add product to warehouse request: {}", addProductToWarehouseRequest);
        warehouseProductService.add(addProductToWarehouseRequest);
    }

    @PostMapping("/api/v1/warehouse/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto check(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        log.debug("Check request product: {}", shoppingCartDto);
        return warehouseProductService.check(shoppingCartDto);
    }

    @GetMapping("/api/v1/warehouse/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto address() {
        log.debug("Address request");
        return warehouseProductService.address();
    }
}
