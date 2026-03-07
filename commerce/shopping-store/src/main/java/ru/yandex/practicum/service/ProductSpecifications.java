package ru.yandex.practicum.service;

import org.springframework.data.jpa.domain.Specification;
import ru.yandex.practicum.model.Product;

public class ProductSpecifications {
    public static Specification<Product> hasCategory(String productCategory) {
        return (root, query, cb) -> {
            if (productCategory == null || productCategory.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("productCategory"), productCategory);
        };
    }
}
