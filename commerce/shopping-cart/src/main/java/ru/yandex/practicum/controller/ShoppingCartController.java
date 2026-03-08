package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ShoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
@Validated
public class ShoppingCartController {
    final private ShoppingCartService shoppingCartService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getShoppingCart(@RequestParam("username") String username) {
        log.debug("Shopping cart request for User: {}", username);
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCart addProductsToCart(@RequestParam("username") String username,
                                          @Valid @RequestBody Map<UUID, Integer> products) {
        log.info("User: {}", username);
        log.info("Add product to shopping cart: {}", products);
        return shoppingCartService.create(username, products);
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam("username") String username) {
        log.debug("Request for deactivate shopping cart for user: {}", username);
        shoppingCartService.delete(username);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCart remove(@RequestParam("username") String username,
                               @RequestBody List<UUID> products) {
        log.debug("Request for remove products from shopping cart: {}", username);
        return shoppingCartService.remove(username, products);
    }

    @PostMapping("/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCart changeQuantity(@RequestParam("username") String username,
                                       @RequestBody ChangeProductQuantityRequest changeProductQuantityRequest) {
        log.debug("Request for remove products from shopping cart: {}", username);
        return shoppingCartService.changeQuantity(username, changeProductQuantityRequest);
    }
}
