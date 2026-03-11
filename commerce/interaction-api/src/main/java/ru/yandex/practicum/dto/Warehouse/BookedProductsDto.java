package ru.yandex.practicum.dto.Warehouse;

import lombok.Data;

@Data
public class BookedProductsDto {
    private Double deliveryWeight = 0.0;
    private Double deliveryVolume = 0.0;
    private Boolean fragile = false;

    public void addWeight(Double weight) {
        this.deliveryWeight = (this.deliveryWeight == null ? 0 : this.deliveryWeight) + weight;
    }

    public void addVolume(Double volume) {
        this.deliveryVolume = (this.deliveryVolume == null ? 0 : this.deliveryVolume) + volume;
    }
}
