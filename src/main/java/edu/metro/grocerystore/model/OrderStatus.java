package edu.metro.grocerystore.model;

public enum OrderStatus {
    PAYMENT_PENDING,
    PROCESSING,
    WAITING_FOR_PICKUP,
    COMPLETED,
    CANCELLED,
    REFUND_PROCESSING,
    REFUNDED
}
