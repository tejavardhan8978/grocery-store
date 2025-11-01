package edu.metro.grocerystore.model;

public enum OrderStatus {
    PAYMENT_PENDING,
    PROCESSING,
    WAITING_FOR_PICKUP,
    COMPLETED,
    CANCELED,
    REFUND_PROCESSING,
    REFUNDED
}
