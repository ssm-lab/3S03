package com.example.shop;

public class PricingService {

    public double calculateSubtotal(Order order) {
        double subtotal = 0.0;
        for (OrderItem item : order.getItems()) {
            subtotal += item.getTotalPrice();
        }
        return subtotal;
    }

    public double calculateTax(double subtotal) {
        if (subtotal < 0) {
            throw new IllegalArgumentException("Subtotal cannot be negative");
        }

        if (subtotal == 0) {
            return 0;
        }

        return subtotal * 0.2; // 20% VAT
    }
}
