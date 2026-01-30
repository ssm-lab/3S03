package com.example.shop;

public class OrderItem {

    private final String name;
    private final int quantity;
    private final double unitPrice;

    public OrderItem(String name, int quantity, double unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return quantity * unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
