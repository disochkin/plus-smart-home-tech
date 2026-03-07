package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "products")
public class ProductWarehouse {
    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "fragile")
    private Boolean fragile;

    @Embedded
    private ProductWarehouseDimension dimension;

    @Column(name = "weight")
    private double weight;

    @Column(name = "quantity")
    private Integer quantity;

    public Double getVolume() {
        return this.dimension.getDepth() * this.dimension.getHeight() * this.dimension.getWidth();
    }
}

