package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ShoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.Warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.Warehouse.AddressDto;
import ru.yandex.practicum.dto.Warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.Warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mapper.ProductWarehouseMapper;
import ru.yandex.practicum.model.ProductWarehouse;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseProductService {
    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];
    private final WarehouseRepository warehouseRepository;
    private final ProductWarehouseMapper productWarehouseMapper;

    public ProductWarehouse create(NewProductInWarehouseRequest newProductInWarehouseRequest) {
        warehouseRepository.findById(newProductInWarehouseRequest.getProductId())
                .ifPresent(product -> {
                    throw new SpecifiedProductAlreadyInWarehouseException(String.format("Продукт с id='%s' уже зарегистрирован на складе",
                            newProductInWarehouseRequest.getProductId()));
                });
        ProductWarehouse productWarehouse =  warehouseRepository.save(productWarehouseMapper.toProductWarehouse(newProductInWarehouseRequest));
        log.debug("Новый товар {} успешно зарегистрован на складе", productWarehouse);
        return productWarehouse;
    }

    public void add(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        UUID productId = addProductToWarehouseRequest.getProductId();

        ProductWarehouse productWarehouse = warehouseRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Продукт id='%s' не найден на складе", productId)));
        productWarehouse.setQuantity(productWarehouse.getQuantity() + addProductToWarehouseRequest.getQuantity());
        warehouseRepository.save(productWarehouse);
    }

    public BookedProductsDto check(ShoppingCartDto shoppingCartDto) {
        BookedProductsDto bookedProductsDto = new BookedProductsDto();

        List<ProductWarehouse> existProducts = warehouseRepository.findAllById(shoppingCartDto.getProducts().keySet());

        Map<UUID, ProductWarehouse> existProductsMap = existProducts.stream()
                .collect(Collectors.toMap(
                        ProductWarehouse::getProductId,
                        Function.identity()));

        Set<UUID> notFoundProducts = shoppingCartDto.getProducts().keySet().stream()
                .filter(key -> !existProductsMap.containsKey(key))
                .collect(Collectors.toSet());

        if (!notFoundProducts.isEmpty()) {
            throw new ProductNotFoundException(
                    String.format("Продукты отсутствуют на складе: %s", notFoundProducts)
            );
        }

        shoppingCartDto.getProducts().keySet().forEach(uuid -> {
            ProductWarehouse existProduct = existProductsMap.get(uuid);
            if (existProduct.getQuantity() < shoppingCartDto.getProducts().get(uuid)) {
                log.info("uuid: {}, existProduct: {}, shoppingCart: {}", uuid, existProduct.getQuantity(),
                        shoppingCartDto.getProducts().get(uuid));
                throw new ProductInShoppingCartLowQuantityInWarehouse(String.format("not enough, %s", uuid));
            }
            bookedProductsDto.addWeight(existProduct.getWeight());
            bookedProductsDto.addVolume(existProduct.getVolume());
            if (existProduct.getFragile()) {
                bookedProductsDto.setFragile(true);
            }
        });
        warehouseRepository.saveAll(existProducts);
        return bookedProductsDto;
    }

    public AddressDto address() {
        return AddressDto.builder().country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS).build();
    }
}
