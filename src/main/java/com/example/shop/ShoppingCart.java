package com.example.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShoppingCart {

    private final Map<String, BigDecimal> items = new HashMap<>();

    public void add(String name, BigDecimal price) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(price, "price");

        // Om samma vara läggs till igen summerar vi priset på samma “rad”
        items.merge(name, price, BigDecimal::add);
    }

    public void remove(String name) {
        items.remove(name);
    }

    public BigDecimal getTotal() {
        return items.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}