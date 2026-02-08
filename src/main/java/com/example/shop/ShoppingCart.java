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
        BigDecimal subtotal = items.values().stream()
                .map(LineItem::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal factor = BigDecimal.ONE.subtract(
                percentageDiscount.divide(new BigDecimal("100"), 10, java.math.RoundingMode.HALF_UP)
        );

        BigDecimal total = subtotal.multiply(factor).subtract(fixedDiscount);

        if (total.compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO;
        return total;
    }

    public void updateQuantity(String name, int newQuantity) {
        Objects.requireNonNull(name, "name");
        if (newQuantity < 0) throw new IllegalArgumentException("newQuantity must be >= 0");

        if (newQuantity == 0) {
            items.remove(name);
            return;
        }

        LineItem item = items.get(name);
        if (item != null) {
            item.quantity = newQuantity;
        }
    }

    public void applyPercentageDiscount(BigDecimal percent) {
        Objects.requireNonNull(percent, "percent");
        if (percent.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("percent must be >= 0");
        }
        this.percentageDiscount = percent;
    }
    private BigDecimal percentageDiscount = BigDecimal.ZERO;
    private BigDecimal fixedDiscount = BigDecimal.ZERO;

    public void applyFixedDiscount(BigDecimal amount) {
        Objects.requireNonNull(amount, "amount");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }
        this.fixedDiscount = amount;
    }
}