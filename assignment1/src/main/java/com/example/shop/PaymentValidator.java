package com.example.shop;

public class PaymentValidator {

    public boolean isPaymentMethodValid(String paymentMethod) {
        if (paymentMethod == null) {
            return false;
        }

        switch (paymentMethod.toLowerCase()) {
            case "card":
            case "paypal":
                return true;
            case "crypto":
                return false;
            default:
                throw new UnsupportedOperationException("Unknown payment method");
        }
    }
}
