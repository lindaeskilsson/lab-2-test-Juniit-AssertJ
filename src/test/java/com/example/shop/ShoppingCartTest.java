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
}