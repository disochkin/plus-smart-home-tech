package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.product.*;
import ru.yandex.practicum.exception.NotFoundException;
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
    public ProductShortDto getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(String.format("Product with id = %d not found", productId)));
        //     .orElseThrow(() -> new EntityNotFoundException(String.format("Product with id = %s not found", productId)));
        return productMapper.toShortDto(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProducts(String category, Pageable pageable) {
        Specification<Product> spec = ProductSpecifications.hasCategory(category);
        return productRepository.findAll(spec, pageable);
    }

    @Transactional
    public ProductShortDto create(NewProductDto newProductDto) {
        Product product = productRepository.save(productMapper.toProduct(newProductDto));
        return productMapper.toShortDto(product);
    }

    @Transactional
    public ProductShortDto update(UpdateProductDto updateProductDto) {
        Product existProduct = productRepository
                .findById(updateProductDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + updateProductDto.getProductId() + " не найдена"));

        Product product = productRepository.save(productMapper.updateProductToProduct(updateProductDto));
        return productMapper.toShortDto(product);
    }

    @Transactional
    public void deactivateProduct(UUID productId) {
        Product existProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + productId + " не найдена"));
        existProduct.setProductState(ProductState.DEACTIVATE);
        productRepository.save(existProduct);
    }

    @Transactional
    public void changeQuantityState(UUID productId, QuantityState quantityState) {
        Product existProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + productId + " не найдена"));
        existProduct.setQuantityState(quantityState);
        productRepository.save(existProduct);
    }
}
