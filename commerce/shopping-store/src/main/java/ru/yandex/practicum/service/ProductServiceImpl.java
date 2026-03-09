package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ShoppingStore.ProductDto;
import ru.yandex.practicum.dto.ShoppingStore.ProductState;
import ru.yandex.practicum.dto.ShoppingStore.QuantityState;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id = %s not found", productId)));
        return productMapper.toShortDto(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProducts(String category, Pageable pageable) {
        Specification<Product> spec = ProductSpecifications.hasCategory(category);
        return productRepository.findAll(spec, pageable);
    }

    @Transactional
    public ProductDto create(ProductDto productDto) {
        Product product = productRepository.save(productMapper.toProduct(productDto));
        return productMapper.toShortDto(product);
    }

    @Transactional
    public ProductDto update(ProductDto productDto) {
        Product existProduct = productRepository
                .findById(productDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Вещь с ID " + productDto.getProductId() + " не найдена"));

        Product product = productRepository.save(productMapper.toProduct(productDto));
        return productMapper.toShortDto(product);
    }

    @Transactional
    public void deactivateProduct(UUID productId) {
        Product existProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Вещь с ID " + productId + " не найдена"));
        existProduct.setProductState(ProductState.DEACTIVATE);
        productRepository.save(existProduct);
    }

    @Transactional
    public void changeQuantityState(UUID productId, QuantityState quantityState) {
        Product existProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Вещь с ID " + productId + " не найдена"));
        existProduct.setQuantityState(quantityState);
        productRepository.save(existProduct);
    }
}
