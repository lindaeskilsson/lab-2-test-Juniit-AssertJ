package com.example.shop;

import java.math.BigDecimal;

public class ShoppingCart {

        private BigDecimal total = BigDecimal.ZERO;

        public void add(String name, BigDecimal price) {
            total = total.add(price);
        }

        public BigDecimal getTotal() {
            return total;
        }
    }
