package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.product.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.product.NewProductInWarehouseRequest;
import ru.yandex.practicum.dto.product.shoppingCart.ShoppingCartDto;
import ru.yandex.practicum.dto.product.warehouse.BookedProductsDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.ProductWarehouseMapper;
import ru.yandex.practicum.model.ProductWarehouse;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.warehouse.AddressDto;

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

    @Transactional
    public ProductWarehouse create(NewProductInWarehouseRequest newProductInWarehouseRequest) {
        return warehouseRepository.save(productWarehouseMapper.toProductWarehouse(newProductInWarehouseRequest));
    }

    @Transactional
    public void add(AddProductToWarehouseRequest addProductToWarehouseRequest) {
        UUID productId = addProductToWarehouseRequest.getProductId();

        ProductWarehouse productWarehouse = warehouseRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id = %s not found", productId)));
        productWarehouse.setQuantity(productWarehouse.getQuantity() + addProductToWarehouseRequest.getQuantity());
        warehouseRepository.save(productWarehouse);
    }

    @Transactional
    public BookedProductsDto check(ShoppingCartDto shoppingCartDto) {
        BookedProductsDto bookedProductsDto = new BookedProductsDto();

        List<ProductWarehouse> existProducts = warehouseRepository.findAllById(shoppingCartDto.getProducts().keySet());

        Map<UUID, ProductWarehouse> existProductsMap = existProducts.stream()
                .collect(Collectors.toMap(
                        ProductWarehouse::getProductId,
                        Function.identity()));

        // Ключи map1, которых НЕТ в map2
        Set<UUID> notFoundProducts = shoppingCartDto.getProducts().keySet().stream()
                .filter(key -> !existProductsMap.containsKey(key))
                .collect(Collectors.toSet());

        if (!notFoundProducts.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Продукты отсутствуют на складе: %s", notFoundProducts)
            );
        }

        shoppingCartDto.getProducts().keySet().forEach(uuid -> {
            ProductWarehouse existProduct = existProductsMap.get(uuid);
            if (existProduct.getQuantity() < shoppingCartDto.getProducts().get(uuid)) {
                log.info("uuid: {}, existProduct: {}, shoppingCart: {}", uuid, existProduct.getQuantity(),
                        shoppingCartDto.getProducts().get(uuid));
                throw new NotFoundException(String.format("not enough, %s", uuid));
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
