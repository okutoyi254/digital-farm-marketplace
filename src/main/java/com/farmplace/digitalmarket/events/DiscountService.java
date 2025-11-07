package com.farmplace.digitalmarket.events;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountService {

        public BigDecimal calculateFinalPrice(BigDecimal originalPrice, int quantity) {
            BigDecimal discountRate = BigDecimal.ZERO;

            if (quantity >= 10 && quantity < 20) {
                discountRate = new BigDecimal("0.10"); // 10% discount
            } else if (quantity >= 20) {
                discountRate = new BigDecimal("0.20"); // 20% discount
            }

            BigDecimal discountAmount = originalPrice.multiply(discountRate);
            BigDecimal finalPrice = originalPrice.multiply(new BigDecimal(quantity)).subtract(discountAmount);

            // Round to 2 decimal places for currency
            return finalPrice.setScale(2, RoundingMode.HALF_UP);
        }
    }



