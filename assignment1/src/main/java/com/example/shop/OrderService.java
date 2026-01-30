package com.example.shop;

public class OrderService {

    private final PricingService pricingService = new PricingService();
    private final DiscountService discountService = new DiscountService();
    private final PaymentValidator paymentValidator = new PaymentValidator();

    public double processOrder(Order order, String discountCode, String paymentMethod) {
        if (!paymentValidator.isPaymentMethodValid(paymentMethod)) {
            order.setStatus(OrderStatus.CANCELLED);
            return 0;
        }

        double subtotal = pricingService.calculateSubtotal(order);
        double discountedTotal = discountService.applyDiscount(subtotal, discountCode);
        double tax = pricingService.calculateTax(discountedTotal);

        order.setStatus(OrderStatus.PAID);
        return discountedTotal + tax;
    }
}
