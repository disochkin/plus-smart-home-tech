package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Entity
@Data
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @Column(name = "uuid")
    private UUID shoppingCartId;

    @Column(name = "username")
    private String user;

    @Column(name = "isActive")
    private Boolean isActive;

    @ElementCollection
    @CollectionTable(
            name = "shopping_cart_items",
            joinColumns = @JoinColumn(name = "shopping_cart_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products;
}
