package com.example.shop;

public class DiscountService {

    public double applyDiscount(double subtotal, String discountCode) {
        if (discountCode == null || discountCode.isBlank()) {
            return subtotal;
        }

        switch (discountCode.toUpperCase()) {
            case "STUDENT10":
                return subtotal * 0.9;
            case "BLACKFRIDAY":
                return subtotal * 0.7;
            case "INVALID":
                throw new IllegalArgumentException("Invalid discount code");
            default:
                return subtotal;
        }
    }
}
