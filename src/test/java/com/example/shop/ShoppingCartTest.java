package com.example.shop;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
        cart.add("Chair", new BigDecimal("100"));
        cart.add("Table", new BigDecimal("550"));

        cart.remove("Table");

        assertThat(cart.getTotal()).isEqualByComparingTo("100");
    }

    //test remove item that does not exist in cart
    @Test
    void removeDoesNothing_whenItemDoesNotExist() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100"));

        cart.remove("DoesNotExist");

        assertThat(cart.getTotal()).isEqualByComparingTo("100");
    }

    // test: price adds upp when adding two of same item
    @Test
    void quantityAccumulates_whenAddingSameItemTwice() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("349"), 2);
        cart.add("Chair", new BigDecimal("349"), 3);

        assertThat(cart.getTotal()).isEqualByComparingTo("1745");
    }

    //test: total price updates when quantity update
    @Test
    void totalUpdates_whenUpdatingQuantity() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100"), 2); // 20

        cart.updateQuantity("Chair", 5);

        assertThat(cart.getTotal()).isEqualByComparingTo("500");
    }

    //test: item removed when update quantity to zero
    @Test
    void itemRemoved_whenUpdatingQuantityToZero() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100"), 2);

        cart.updateQuantity("Chair", 0);

        assertThat(cart.getTotal()).isEqualByComparingTo("0");
    }

    // test: percentage discount added when discounts set
    @Test
    void percentageDiscountApplied_whenDiscountSet() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100.00"));

        cart.applyPercentageDiscount(new BigDecimal("10")); // 10%
        assertThat(cart.getTotal()).isEqualByComparingTo("90.00");
    }

    // test: persantage discount, throw exeption when percentage is negative
    @Test
    void applyPercentageDiscountThrows_whenPercentIsNegative() {
        ShoppingCart cart = new ShoppingCart();

        assertThatThrownBy(() -> cart.applyPercentageDiscount(new BigDecimal("-1")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // test: total price can never be less than zero
    @Test
    void totalNeverBelowZero_whenDiscountExceeds100Percent() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100.00"));

        cart.applyPercentageDiscount(new BigDecimal("150")); // 150%
        assertThat(cart.getTotal()).isEqualByComparingTo("0");
    }

    // test set fixed discount
    @Test
    void fixedDiscountApplied_whenDiscountSet() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100.00"));

        cart.applyFixedDiscount(new BigDecimal("25.00"));

        assertThat(cart.getTotal()).isEqualByComparingTo("75.00");
    }

    // kanttest: throws expetion when quantity is 0 or negative
    @Test
    void addThrows_whenQuantityIsZeroOrNegative() {
        ShoppingCart cart = new ShoppingCart();

        assertThatThrownBy(() -> cart.add("Chair", new BigDecimal("100"), 0))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> cart.add("Chair", new BigDecimal("100"), -1))
                .isInstanceOf(IllegalArgumentException.class);

    }
    // Kanttest: Expetion throws when quantatiy is negative
    @Test
    void updateQuantityThrows_whenQuantityIsNegative() {
        ShoppingCart cart = new ShoppingCart();
        cart.add("Chair", new BigDecimal("100"), 1);

        assertThatThrownBy(() -> cart.updateQuantity("Chair", -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

}