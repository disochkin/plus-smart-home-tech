package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.clients.WarehouseClient;
import ru.yandex.practicum.dto.ShoppingCart.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.Warehouse.BookedProductsDto;
import ru.yandex.practicum.exceptions.DeactivateCartException;
import ru.yandex.practicum.exceptions.NoProductsInShoppingCartException;
import ru.yandex.practicum.exceptions.NotAuthorizedUserException;
import ru.yandex.practicum.exceptions.ShoppingCartNotFoundException;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final WarehouseClient warehouseClient;
    private final ShoppingCartMapper shoppingCartMapper;

    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(String username) {
        checkUsername(username);
        ShoppingCart shoppingCart = getOrCreate(username);
        return shoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    private ShoppingCart getOrCreate(String username) {
        return shoppingCartRepository.findByUserIgnoreCase(username)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.setUser(username);;
                    shoppingCartRepository.save(shoppingCart);
                    log.info("Корзина не найдена, создали новую uuid='{}'", shoppingCart.getShoppingCartId());
                    return shoppingCart;
                });
    }

    @Transactional
    public ShoppingCart addProductsToShoppingCartByUserName(String username, Map<UUID, Integer> newProducts) {
        checkUsername(username);
        log.info("Поиск существующей корзины по username='{}'", username);
        ShoppingCart shoppingCart = this.getOrCreate(username);
        log.info("Используем корзину uuid='{}'", shoppingCart.getShoppingCartId());
        checkShoppingCartState(shoppingCart);
        Map<UUID, Integer> existProductsInCart = shoppingCart.getProducts();
        log.info("Исходное содержимое корзины uuid='{}', products='{}'", shoppingCart.getShoppingCartId(), existProductsInCart);

        // Складываем мапы по uuid
        Map<UUID, Integer> totalProducts = Stream.of(existProductsInCart, newProducts)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));
        log.info("Обновленное содержимое корзины {}", totalProducts);
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(
                shoppingCart.getShoppingCartId(),
                totalProducts
        );
        BookedProductsDto bookedProductsDto = warehouseClient.check(shoppingCartDto);
        log.info("Проверили наличие на складе, заказ {}", bookedProductsDto);
        shoppingCart.setProducts(totalProducts);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    public void deactivateShoppingCart(String username) {
        checkUsername(username);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIgnoreCase(username)
                .orElseThrow(() -> new ShoppingCartNotFoundException(String.format("Корзина не найдена, user: %s", username)));
        if (shoppingCart.getIsActive()) {
            shoppingCart.setIsActive(false);
            shoppingCartRepository.save(shoppingCart);
            log.info("Корзина id='{}', пользователь='{}' деактивирована", shoppingCart.getShoppingCartId(),
                                                                          shoppingCart.getUser());
        }
    }

    @Transactional
    public ShoppingCart remove(String username, List<UUID> productsToRemove) {
        checkUsername(username);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIgnoreCase(username)
                .orElseThrow(() -> new ShoppingCartNotFoundException(String.format("Корзина не найдена, user: %s", username)));
        checkShoppingCartState(shoppingCart);
        List<UUID> missingKeys = productsToRemove.stream()
                .filter(uuid -> !shoppingCart.getProducts().containsKey(uuid))
                .collect(Collectors.toList());
        if (!missingKeys.isEmpty()) {
            throw new NoProductsInShoppingCartException("Продукты для удаления не найдены в корзине: " + missingKeys);
        }
        productsToRemove.forEach(shoppingCart.getProducts().keySet()::remove);
        shoppingCartRepository.save(shoppingCart);
        log.info("Обновленная корзина сохранена {}", shoppingCart);
        return shoppingCart;
    }

    @Transactional
    public ShoppingCart changeQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest) {
        checkUsername(username);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserIgnoreCase(username)
                .orElseThrow(() -> new ShoppingCartNotFoundException(String.format("Корзина не найдена, user: %s", username)));
        checkShoppingCartState(shoppingCart);

        if (!shoppingCart.getProducts().containsKey(changeProductQuantityRequest.getProductId())) {
            throw new RuntimeException(String.format("Продукт %s не найден в корзине", changeProductQuantityRequest.getProductId()));
        }

        Map<UUID, Integer> products = shoppingCart.getProducts();

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(
                shoppingCart.getShoppingCartId(),
                products);

        products.put(changeProductQuantityRequest.getProductId(),
                changeProductQuantityRequest.getNewQuantity());
        log.info("Обновленное содержимое корзины {}", products);
        BookedProductsDto bookedProductsDto = warehouseClient.check(shoppingCartDto);
        log.info("Проверили наличие на складе, заказ {}", bookedProductsDto);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    private void checkUsername(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Недопустимое имя пользователя");
        }
    }

    private void checkShoppingCartState(ShoppingCart shoppingCart) {
        if (!shoppingCart.getIsActive()) {
            throw new DeactivateCartException(String.format("Корзина uuid='%s', user='%s' не активна",
                    shoppingCart.getShoppingCartId(), shoppingCart.getUser()));
        }
    }


}
