package com.example.shop;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ShoppingCart {

    private static final class LineItem {
        private final BigDecimal unitPrice;
        private int quantity;

        private LineItem(BigDecimal unitPrice, int quantity) {
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        private BigDecimal total() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    private final Map<String, LineItem> items = new HashMap<>();

    public void add(String name, BigDecimal price) {
        add(name, price, 1);
    }

    public void add(String name, BigDecimal price, int quantity) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(price, "price");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");

        LineItem existing = items.get(name);
        if (existing == null) {
            items.put(name, new LineItem(price, quantity));
        } else {
            // antag: samma name = samma pris i våra tester
            existing.quantity += quantity;
        }
    }

    public void remove(String name) {
        items.remove(name);
    }

    public BigDecimal getTotal() {
        return items.values().stream()
                .map(LineItem::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}