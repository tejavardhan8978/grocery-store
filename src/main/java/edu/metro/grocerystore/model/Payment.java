package edu.metro.grocerystore.model;

import jakarta.persistence.*;

@Entity
@Table(name="payments")
public class Payment {

    @Id
    @Column(name="payment_id")
    Integer paymentId;

    //Ideally one order to one payment and one payment to one order
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,insertable=false, updatable=false)
    private Order order;

    //Ref: https://medium.com/@iam03rv/best-way-to-map-an-enum-type-with-jpa-and-hibernate-with-examples-9974c856f33d
    @Enumerated(EnumType.STRING)
    @Column(length=15)
    private PaymentStatus paymentStatus;

    @Column(name="paid_amount")
    private Double paidAmount;




}
