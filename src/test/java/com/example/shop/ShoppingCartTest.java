package com.example.shop;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ShoppingCartTest {

    // Test: total is zero when cart is still empty
    @Test
    void totalIsZero_whenCartIsEmpty() {
        ShoppingCart cart = new ShoppingCart();

        assertThat(cart.getTotal()).isEqualByComparingTo("0");
    }

    //test: cart increases when adding item to cart.
    @Test
    void totalIncreases_whenAddingItem(){
        ShoppingCart cart = new ShoppingCart();
        cart.add("Lamp", new BigDecimal(199));

        assertThat(cart.getTotal()).isEqualByComparingTo("199");
    }

    // test: add more than one item to cart and total accumulates
    @Test
    void totalAccumulates_whenAddingMultipleItems() {
        ShoppingCart cart = new ShoppingCart();

        cart.add("Lamp", new BigDecimal("199"));
        cart.add("Chair", new BigDecimal("349"));

        assertThat(cart.getTotal()).isEqualByComparingTo("548");
    }

    //test: removen item and decrease total
    @Test
    void totalDecreases_whenRemovingExistingItem() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Apple", new BigDecimal("10.00"));
        cart.add("Banana", new BigDecimal("5.50"));

        cart.remove("Banana");

        assertThat(cart.getTotal()).isEqualByComparingTo("10.00");
    }

    //test remove item that does not exist in cart
    @Test
    void removeDoesNothing_whenItemDoesNotExist() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Apple", new BigDecimal("10.00"));

        cart.remove("DoesNotExist");

        assertThat(cart.getTotal()).isEqualByComparingTo("10.00");
    }

    // test: price adds upp when adding two of same item
    @Test
    void quantityAccumulates_whenAddingSameItemTwice() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("349"), 2);
        cart.add("Chair", new BigDecimal("349"), 3);

        assertThat(cart.getTotal()).isEqualByComparingTo("1745");
    }

}