package ru.yandex.practicum.clients;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.ShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.Warehouse.BookedProductsDto;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto check(@Valid @RequestBody ShoppingCartDto shoppingCartDto);
}