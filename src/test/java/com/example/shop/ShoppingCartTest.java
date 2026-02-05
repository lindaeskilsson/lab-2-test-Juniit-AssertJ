package com.example.shop;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ShoppingCartTest {

    @Test
    void totalIsZero_whenCartIsEmpty() {
        ShoppingCart cart = new ShoppingCart();

        assertThat(cart.getTotal()).isEqualByComparingTo("0");
    }
}