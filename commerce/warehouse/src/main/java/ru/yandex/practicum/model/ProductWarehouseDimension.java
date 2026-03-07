package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ProductWarehouseDimension {
    @Column(name = "width")
    private double width;

    @Column(name = "height")
    private double height;

    @Column(name = "depth")
    private double depth;

    public ProductWarehouseDimension() {
    }

    public ProductWarehouseDimension(double width, double height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

}
