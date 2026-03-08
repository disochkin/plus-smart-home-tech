package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.dto.product.shoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.product.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.product.warehouse.BookedProductsDto;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final WarehouseClient warehouseClient;
    private final ShoppingCartMapper shoppingCartMapper;

    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException(String.format("Shopping cart for user: %s not found", username)));
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }


    private ShoppingCart getOrCreate(String username) {
        return shoppingCartRepository.findByUserIgnoreCase(username)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setShoppingCartId(UUID.randomUUID());
                    shoppingCart.setUser(username);
                    shoppingCart.setIsActive(true);
                    shoppingCart.setProducts(new HashMap<>());
                    shoppingCartRepository.save(shoppingCart);
                    return shoppingCart;
                });
    }

    @Transactional
    public ShoppingCart create(String username, Map<UUID, Integer> products) {

        ShoppingCart shoppingCart = this.getOrCreate(username);
        if (!shoppingCart.getIsActive()) {
            throw new RuntimeException();
        }

        Map<UUID, Integer> existProductsInCart = shoppingCart.getProducts();

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(
                shoppingCart.getShoppingCartId(),
                products
        );

       BookedProductsDto bookedProductsDto = warehouseClient.check(shoppingCartDto);

        Map<UUID, Integer> result = Stream.of(existProductsInCart, products)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));

        shoppingCart.setProducts(result);

        return shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    public void delete(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException(String.format("Shopping cart for user: %d not found", username)));
        if (shoppingCart.getIsActive()) {
            shoppingCart.setIsActive(false);
        }
    }

    @Transactional
    public ShoppingCart remove(String username, List<UUID> products) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException(String.format("Shopping cart for user: %d not found", username)));
        if (!shoppingCart.getIsActive()) {
            throw new RuntimeException();
        }
        List<UUID> missingKeys = products.stream()
                .filter(uuid -> !shoppingCart.getProducts().containsKey(uuid))
                .collect(Collectors.toList());
        if (!missingKeys.isEmpty()) {
            throw new RuntimeException("Product not found " + missingKeys);
        }
        products.forEach(shoppingCart.getProducts().keySet()::remove);

        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    @Transactional
    public ShoppingCart changeQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException(String.format("Shopping cart for user: %d not found", username)));
        if (!shoppingCart.getIsActive()) {
            throw new RuntimeException();
        }

        if (!shoppingCart.getProducts().containsKey(changeProductQuantityRequest.getProductId())) {
            throw new RuntimeException("Product not found " + changeProductQuantityRequest.getProductId());
        }

        Map<UUID, Integer> products = shoppingCart.getProducts();

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(
                shoppingCart.getShoppingCartId(),
                products);

        BookedProductsDto bookedProductsDto = warehouseClient.check(shoppingCartDto);
        products.put(changeProductQuantityRequest.getProductId(),
                changeProductQuantityRequest.getNewQuantity());
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

}
