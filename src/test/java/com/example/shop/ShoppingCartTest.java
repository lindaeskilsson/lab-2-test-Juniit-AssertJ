package com.example.shop;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ShoppingCartTest {

    // RED: total should be 0 for an empty cart
    // GREEN (feat): add ShoppingCart with getTotal() returning 0
    @Test
    void totalIsZero_whenCartIsEmpty() {
        ShoppingCart cart = new ShoppingCart();

        assertThat(cart.getTotal()).isEqualByComparingTo("0");
    }

    // RED: total should increase when adding one item
    // GREEN (feat): implement add(name, price) and sum totals in getTotal()
    @Test
    void totalIncreases_whenAddingItem(){
        ShoppingCart cart = new ShoppingCart();
        cart.add("Lamp", new BigDecimal(199));

        assertThat(cart.getTotal()).isEqualByComparingTo("199");
    }

    // TEST: adding multiple items accumulates total
    // FEAT (Green): store multiple items internally (Map) and sum all line totals
    @Test
    void totalAccumulates_whenAddingMultipleItems() {
        ShoppingCart cart = new ShoppingCart();

        cart.add("Lamp", new BigDecimal("199"));
        cart.add("Chair", new BigDecimal("349"));

        assertThat(cart.getTotal()).isEqualByComparingTo("548");
    }

    // TEST: removing an existing item decreases total
    // FEAT (Green): implement remove(name) and ensure total recalculates from stored items
    @Test
    void totalDecreases_whenRemovingExistingItem() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100"));
        cart.add("Table", new BigDecimal("550"));

        cart.remove("Table");

        assertThat(cart.getTotal()).isEqualByComparingTo("100");
    }

    // TEST: removing an item that does not exist should do nothing
    // FEAT (Green): remove(name) should be safe (no crash) and not affect total
    @Test
    void removeDoesNothing_whenItemDoesNotExist() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100"));

        cart.remove("DoesNotExist");

        assertThat(cart.getTotal()).isEqualByComparingTo("100");
    }

    // TEST: total updates when quantity is updated
    // FEAT (Green): implement updateQuantity(name, newQuantity)
    @Test
    void totalUpdates_whenUpdatingQuantity() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100"), 2); // 20

        cart.updateQuantity("Chair", 5);

        assertThat(cart.getTotal()).isEqualByComparingTo("500");
    }

    // TEST: setting quantity to zero removes the item
    // FEAT (Green): updateQuantity(name, 0) should remove the item
    @Test
    void itemRemoved_whenUpdatingQuantityToZero() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100"), 2);

        cart.updateQuantity("Chair", 0);

        assertThat(cart.getTotal()).isEqualByComparingTo("0");
    }

    // TEST: percentage discount reduces total
    // FEAT (Green): implement applyPercentageDiscount(percent) and apply it in getTotal()
    @Test
    void percentageDiscountApplied_whenDiscountSet() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100.00"));

        cart.applyPercentageDiscount(new BigDecimal("10")); // 10%
        assertThat(cart.getTotal()).isEqualByComparingTo("90.00");
    }

    // TEST: negative percentage discount should throw exception
    // FEAT (Green): validate percent >= 0 in applyPercentageDiscount
    @Test
    void applyPercentageDiscountThrows_whenPercentIsNegative() {
        ShoppingCart cart = new ShoppingCart();

        assertThatThrownBy(() -> cart.applyPercentageDiscount(new BigDecimal("-1")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // TEST: total price should never be below zero (even with huge discounts)
    // FEAT (Green): clamp total to 0 in getTotal()
    @Test
    void totalNeverBelowZero_whenDiscountExceeds100Percent() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100.00"));

        cart.applyPercentageDiscount(new BigDecimal("150")); // 150%
        assertThat(cart.getTotal()).isEqualByComparingTo("0");
    }

    // EDGE TEST: quantity 0 or negative should throw exception when adding
    @Test
    void addThrows_whenQuantityIsZeroOrNegative() {
        ShoppingCart cart = new ShoppingCart();

        assertThatThrownBy(() -> cart.add("Chair", new BigDecimal("100"), 0))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> cart.add("Chair", new BigDecimal("100"), -1))
                .isInstanceOf(IllegalArgumentException.class);

    }

    // EDGE TEST: updating quantity to a negative value should throw exception
    @Test
    void updateQuantityThrows_whenQuantityIsNegative() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100"), 1);

        assertThatThrownBy(() -> cart.updateQuantity("Chair", -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}